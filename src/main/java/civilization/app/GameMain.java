package civilization.app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import civilization.astar.AStar;
import civilization.civilization.Civilization;
import civilization.civilization.building.BuildingType;
import civilization.human.Human;
import civilization.human.HumanAction;
import civilization.human.Scout;
import civilization.map.Area;
import civilization.map.Map;

public class GameMain extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2318489391810394155L;

	public static int WIDTH = 800;
	public static int HEIGHT = 600;

	private Thread thread;
	private boolean running;

	private BufferedImage image;
	private Graphics2D g;

	private int FPS = 60;
	private double averageFPS;

	private Map map;
	private Civilization civilization;
	private AStar aStar;
	private int humanId;
	private boolean buildingState;
	private boolean createHuman;
	private Font standardFont;
	private Font bigFont;

	public GameMain() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void init() {
		map = Map.getInstance();
		civilization = new Civilization();
		aStar = AStar.getInstance();
		humanId = 1;
		buildingState=false;
		createHuman=false;
		standardFont = g.getFont();
		bigFont = new Font("TimesRoman", Font.PLAIN, 20); 
		centerMap();
		createPeople();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}

	public void run() {
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		init();

		long startTime;
		long URDTimeMillis;
		long waitTime;

		long targetTime = 1000 / FPS;
		while (running) {
			int frameCount = 0;
			long totalTime = 0;
			startTime = System.nanoTime();

			gameUpdate();
			gameRender();
			gameDraw();

			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis;

			try {
				Thread.sleep(waitTime);
			} catch (Exception e) {
			}

			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == FPS) {
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
			}
		}
	}

	private synchronized void gameUpdate() {
		civilization.update();
	}

	private synchronized void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	private synchronized void gameRender() {
		map.draw(g);
		civilization.draw(g);
		if(buildingState){
			g.setFont(bigFont);
			g.drawString("Building...", 10, 20);
			g.setFont(standardFont);
		}
		if(createHuman){
			g.setFont(bigFont);
			g.drawString("Giving birth...", 10, 20);
			g.setFont(standardFont);
		}
	}

	public void mouseDragged(MouseEvent ev) {
		int mx = ev.getX();
		int my = ev.getY();
		if (SwingUtilities.isRightMouseButton(ev) && (mx > 150 && mx < WIDTH && my > 20 && my < HEIGHT)) {
			int distance = 3;

			Area[][] areas = map.getAreas();
			int width = map.getWIDTH();
			int height = map.getHEIGHT();
			double distanceX = (Math.sqrt(Math.pow(((WIDTH / 2 + 150) - mx), 2))) / (WIDTH / 2 + 150) * distance;
			double distanceY = (Math.sqrt(Math.pow(((HEIGHT / 2 + 30) - my), 2))) / (HEIGHT / 2 + 30) * distance;
			if (mx > WIDTH / 2 + 120)
				distanceX *= -1;
			if (my > HEIGHT / 2 + 30)
				distanceY *= -1;
			boolean canMoveX = false;
			boolean canMoveY = false;
			if (areas[width - 1][0].getX() + distanceX > WIDTH - 30 && areas[0][0].getX() + distanceX < 150)
				canMoveX = true;
			if (areas[0][height - 1].getY() + distanceY > HEIGHT - 30 && areas[0][0].getY() + distanceY < 30)
				canMoveY = true;

			for (Area[] areaRow : areas) {
				for (Area area : areaRow) {
					if (canMoveX)
						area.setX(area.getX() + (int) distanceX);
					if (canMoveY)
						area.setY(area.getY() + (int) distanceY);
				}
			}
		}
	}

	public void mouseMoved(MouseEvent ev) {
		if(buildingState){
			mouseMoveBuilding(ev);
		}else{
			if(createHuman){
				mouseMoveCreateHuman(ev);
			}else{
				mouseMoveHuman(ev);
			}
			
		}
	}
	
	private void mouseMoveHuman(MouseEvent ev){
		Area[][] areas = map.getAreas();
		for (Area[] areaRow : areas) {
			for (Area ar : areaRow) {
				if (ar.isHighlight()) {
					ar.setHighlight(false);
					break;
				}
			}
		}
		Area area = map.getAreas()[0][0];
		int indX = (Math.abs(area.getX() - 150) + ev.getX() - 150) / 30;
		int indY = (Math.abs(area.getY() - 30) + ev.getY() - 30) / 30;
		if (indX >= 0 && indY >= 0)
			map.getAreas()[indX][indY].setHighlight(true);
	}
	
	private void mouseMoveCreateHuman(MouseEvent ev){
		Area[][] areas = map.getAreas();
		for (Area[] areaRow : areas) {
			for (Area ar : areaRow) {
				if (ar.isHiglightCreateHuman()) {
					ar.setHiglightCreateHuman(false);
					break;
				}
			}
		}
		Area area = map.getAreas()[0][0];
		int indX = (Math.abs(area.getX() - 150) + ev.getX() - 150) / 30;
		int indY = (Math.abs(area.getY() - 30) + ev.getY() - 30) / 30;
		if (indX >= 0 && indY >= 0 && map.getAreas()[indX][indY].getBuildingType()!=null && map.getAreas()[indX][indY].getBuildingType().equals(BuildingType.WAREHOUSE))
			map.getAreas()[indX][indY].setHiglightCreateHuman(true);
	}
	
	private void mouseMoveBuilding(MouseEvent ev){
		Area[][] areas = map.getAreas();
		for (Area[] areaRow : areas) {
			for (Area ar : areaRow) {
				if (!ar.getHiglightBuilding().equals(BuildingType.NONE)) {
					ar.setHiglightBuilding(BuildingType.NONE);
					break;
				}
			}
		}
		Area area = map.getAreas()[0][0];
		int indX = (Math.abs(area.getX() - 150) + ev.getX() - 150) / 30;
		int indY = (Math.abs(area.getY() - 30) + ev.getY() - 30) / 30;
		if (indX >= 0 && indY >= 0 && map.getAreas()[indX][indY].getCost()>=0){
			map.getAreas()[indX][indY].setHiglightBuilding(BuildingType.WAREHOUSE);
		}
			
	}

	public void mouseClicked(MouseEvent ev) {
		int mx = ev.getX();
		int my = ev.getY();

		if(buildingState){
			buildClick(mx,my);
		}else{
			if(createHuman){
				createHuman(mx,my);
			}else{
				humanClick(mx,my);
			}
		}
		
	}
	
	private void humanClick(int mx, int my){
		if (mx > 150 && my > 30) {
			Area clickedArea = null;
			Area[][] areas = map.getAreas();
			for (Area[] areaRow : areas) {
				for (Area area : areaRow) {
					if (area.getX() < mx && area.getX() + 30 > mx && area.getY() < my && area.getY() + 30 > my) {
						clickedArea = area;
						break;
					}
				}
			}
			if (clickedArea!=null && clickedArea.getCost() > 0) {
				List<Human> activeHumanList = civilization.getPeople().stream().filter(x -> x.isActive())
						.collect(Collectors.toList());
				if (activeHumanList != null && activeHumanList.size() > 0) {
					Human activeHuman = activeHumanList.get(0);

					if (activeHuman.getArea().equals(clickedArea)) {
						activeHuman.setActive(false);
					} else {
						activeHuman.setPath(aStar.generatePath(activeHuman.getArea(), clickedArea));
						activeHuman.setAction(HumanAction.GO);
						activeHuman.setActive(false);
					}
				} else {
					List<Human> toActive = civilization.getPeople().stream()
							.filter(x -> x.getArea().getX() < mx && x.getArea().getX() + 30 > mx
									&& x.getArea().getY() < my && x.getArea().getY() + 30 > my)
							.collect(Collectors.toList());
					if (toActive != null && toActive.size() > 0) {
						toActive.get(0).setActive(true);
					}
				}
			}
		} else if (mx <= 150 && my >= 100) {
			int index = (my - 100) / 30 + 1;
			List<Human> human = civilization.getPeople().stream().filter(x -> x.getId() == index)
					.collect(Collectors.toList());
			List<Human> active = civilization.getPeople().stream().filter(x -> x.isActive())
					.collect(Collectors.toList());
			if (!human.isEmpty()) {
				if (!active.isEmpty()) {
					if (human.get(0).equals(active.get(0))) {
						human.get(0).setActive(true);
					} else {
						active.get(0).setActive(false);
						human.get(0).setActive(true);
					}
				} else {
					human.get(0).setActive(true);
				}
			}

		}
	}
	
	private void createHuman(int mx, int my){
		if (mx > 150 && my > 30) {
			Area clickedArea = null;
			Area[][] areas = map.getAreas();
			for (Area[] areaRow : areas) {
				for (Area area : areaRow) {
					if (area.getX() < mx && area.getX() + 30 > mx && area.getY() < my && area.getY() + 30 > my) {
						clickedArea = area;
						break;
					}
				}
			}
			if (clickedArea!=null && map.getBuildings().get(clickedArea)!=null && map.getBuildings().get(clickedArea).getType().equals(BuildingType.WAREHOUSE)) {
				int foodCap = civilization.getCity().getFood();
				if(foodCap>=100 && civilization.getPeople().size()<17){
					civilization.getCity().setFood(foodCap-100);
					civilization.addHuman(new Scout(clickedArea, humanId++));
					clickedArea.setHiglightCreateHuman(false);
					createHuman=false;
				}else{
					createHuman=false;
				}
			}
		} 
	}
	
	private void buildClick(int mx, int my){
		if (mx > 150 && my > 30) {
			Area clickedArea = null;
			Area[][] areas = map.getAreas();
			for (Area[] areaRow : areas) {
				for (Area area : areaRow) {
					if (area.getX() < mx && area.getX() + 30 > mx && area.getY() < my && area.getY() + 30 > my) {
						clickedArea = area;
						break;
					}
				}
			}
			if (clickedArea!=null && clickedArea.getCost()>=0 && map.getBuildings().get(clickedArea)==null) {
				map.addNewBuilding(clickedArea, BuildingType.WAREHOUSE);
				int woodCap = civilization.getCity().getWood()-200;
				civilization.getCity().setWood(woodCap);
				clickedArea.setHiglightBuilding(BuildingType.NONE);
				buildingState=false;
			}
		} 
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private void centerMap() {
		Area firstBuilding = map.getBuildings().entrySet().iterator().next().getKey();
		int distanceX = firstBuilding.getX();
		int distanceY = firstBuilding.getY();

		if (distanceX < 475) {
			distanceX = 0;
		} else if (distanceX > map.getWIDTH() * 30 + 150 - 325) {
			distanceX = map.getWIDTH() * 30 + 150 - 650;
		} else {
			distanceX = distanceX - 325;
		}
		if (distanceY < 315) {
			distanceY = 0;
		} else if (distanceY > map.getHEIGHT() * 30 + 30 - 285) {
			distanceY = map.getHEIGHT() * 30 + 30 - 570;
		} else {
			distanceY = distanceY - 285;
		}
		Area[][] areas = map.getAreas();

		for (Area[] areaRow : areas) {
			for (Area area : areaRow) {
				area.setX(area.getX() - distanceX);
				area.setY(area.getY() - distanceY);
			}
		}

	}

	private void createPeople() {
		civilization.addHuman(new Scout(map.getBuildings().entrySet().iterator().next().getKey(), humanId++));
		civilization.addHuman(new Scout(map.getBuildings().entrySet().iterator().next().getKey(), humanId++));
	}

	@Override
	public void keyPressed(KeyEvent ev) {
		switch(ev.getKeyCode()){
		case KeyEvent.VK_SPACE:
			int foodCap = civilization.getCity().getFood();
			if(foodCap>=100){
				createHuman=true;
				buildingState=false;
				Area[][] areas = map.getAreas();
				for (Area[] areaRow : areas) {
					for (Area ar : areaRow) {
						if (ar.isHighlight()) {
							ar.setHighlight(false);
						}
						if (!ar.getHiglightBuilding().equals(BuildingType.NONE)) {
							ar.setHiglightBuilding(BuildingType.NONE);
						}
						if (ar.isHiglightCreateHuman()) {
							ar.setHiglightCreateHuman(false);
						}
					}
				}
			}
			break;
		case KeyEvent.VK_B:
			int woodCap = civilization.getCity().getWood();
			if(woodCap>=200){
				buildingState=true;
				createHuman=false;
				Area[][] areas = map.getAreas();
				for (Area[] areaRow : areas) {
					for (Area ar : areaRow) {
						if (ar.isHighlight()) {
							ar.setHighlight(false);
						}
						if (!ar.getHiglightBuilding().equals(BuildingType.NONE)) {
							ar.setHiglightBuilding(BuildingType.NONE);
						}
						if (ar.isHiglightCreateHuman()) {
							ar.setHiglightCreateHuman(false);
						}
					}
				}
			}
			break;
		case KeyEvent.VK_ESCAPE:
			buildingState=false;
			createHuman=false;
			Area[][] areas = map.getAreas();
			for (Area[] areaRow : areas) {
				for (Area ar : areaRow) {
					if (ar.isHighlight()) {
						ar.setHighlight(false);
					}
					if (!ar.getHiglightBuilding().equals(BuildingType.NONE)) {
						ar.setHiglightBuilding(BuildingType.NONE);
					}
					if (ar.isHiglightCreateHuman()) {
						ar.setHiglightCreateHuman(false);
					}
				}
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent ev) {
		
		
	}

}
