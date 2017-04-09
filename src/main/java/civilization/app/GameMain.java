package civilization.app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import civilization.astar.AStar;
import civilization.civilization.Civilization;
import civilization.human.Human;
import civilization.human.Scout;
import civilization.map.Area;
import civilization.map.Map;

public class GameMain extends JPanel implements Runnable, MouseListener, MouseMotionListener {

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
		centerMap();
		createPeople();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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

	public void mouseClicked(MouseEvent ev) {
		int mx = ev.getX();
		int my = ev.getY();

		if (mx > 150 && my > 30) {
			Area clickedArea = null;
			Area[][] areas = map.getAreas();
			for (Area[] areaRow : areas) {
				for (Area area : areaRow) {
					if(area.getX() < mx && area.getX() + 30 > mx
								&& area.getY() < my && area.getY() + 30 > my){
						clickedArea = area;
						break;
					}
				}
			}
			if(clickedArea.getCost()>0){
				List<Human> activeHumanList = civilization.getPeople().stream().filter(x -> x.isActive()).collect(Collectors.toList());
				if (activeHumanList != null && activeHumanList.size()>0) {
					Human activeHuman = activeHumanList.get(0);
					
					if(activeHuman.getArea().equals(clickedArea)){
						activeHuman.setActive(false);
					}else{
						activeHuman.setPath(aStar.generatePath(activeHuman.getArea(), clickedArea));
						System.out.println(activeHuman.getPath());
						activeHuman.setActive(false);
					}
				} else {
					List<Human> toActive = civilization.getPeople()
							.stream().filter(x -> x.getArea().getX() < mx && x.getArea().getX() + 30 > mx
									&& x.getArea().getY() < my && x.getArea().getY() + 30 > my).collect(Collectors.toList());
					if(toActive!=null && toActive.size()>0){
						toActive.get(0).setActive(true);
					}
				}
			}
		}

//		List<Human> humanSelected = civilization.getPeople().stream().filter(x -> x.isActive())
//				.collect(Collectors.toList());
//		if (humanSelected != null && !humanSelected.isEmpty()) {
//			Human human = humanSelected.get(0);
//			Area area = null;
//			Area[][] areas = map.getAreas();
//			for (Area[] areaRow : areas) {
//				for (Area areaCol : areaRow) {
//					if ((areaCol.getX() < mx && areaCol.getX() + 30 > mx)
//							&& (areaCol.getY() < my && areaCol.getY() + 30 > my) && areaCol.getCost() > 0) {
//						area = areaCol;
//						break;
//					}
//				}
//			}
//			if (area != null) {
//				human.setPath(aStar.generatePath(human.getArea(), area));
//				System.out.println(human.getPath());
//				human.setActive(false);
//			} else {
//				human.setActive(true);
//			}
//		}
//
//		List<Human> toSelect = civilization.getPeople().stream().filter(x -> {
//			Area area = x.getArea();
//			return area.getX() < mx && area.getX() + 30 > mx;
//		}).collect(Collectors.toList());
//
//		if (toSelect != null && !toSelect.isEmpty())
//			toSelect.get(0).setActive(true);

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
		civilization.addHuman(new Scout(map.getBuildings().entrySet().iterator().next().getKey()));
	}

}
