package civilization.map;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import civilization.civilization.building.Building;
import civilization.civilization.building.BuildingType;
import civilization.civilization.building.Warehouse;
import civilization.image.ImageManager;
import civilization.json.JsonService;

public class Map {
	private static Map instance;
	private JsonService jsonService;
	private final int WIDTH;
	private final int HEIGHT;
	private final int LAST_AREA_INDEX;
	private Area[][] areas;
	private java.util.Map<Area, Building> buildings;
	private ImageManager imageManager;
	
	public static synchronized Map getInstance(){
		return instance==null ? instance = new Map() : instance;
	}

	public Map() {
		jsonService = JsonService.getInstance();
		imageManager = ImageManager.getInstance();
		
		WIDTH = 100;
		HEIGHT = 100;
		LAST_AREA_INDEX = 4;
		areas = new Area[WIDTH][HEIGHT];
		Random rand = new Random();
		// GENERATE AREAS
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				ArrayList<Integer> randomList = new ArrayList<Integer>();
				if (i == 0 && j == 0) { // FIRST ELEMENT - EVERY AREA TYPE POSSIBLE
					for (int k = 0; k <= LAST_AREA_INDEX; k++) {
						randomList.add(k);
					}
				} else if (i == 0 && j != 0) {
					randomList.addAll(areas[i][j - 1].getAvailableNeighbors());
				} else if (j == 0 && i != 0) {
					randomList.addAll(areas[i - 1][j].getAvailableNeighbors());
				} else if (i > 0 && j > 0) {
					for (int k = 0; k < areas[i - 1][j].getAvailableNeighbors().size(); k++) {
						for (int l = 0; l < areas[i][j - 1].getAvailableNeighbors().size(); l++) {
							if (areas[i - 1][j].getAvailableNeighbors().get(k) == areas[i][j - 1]
									.getAvailableNeighbors().get(l)) {
								randomList.add(areas[i - 1][j].getAvailableNeighbors().get(k));
							}
						}
					}
				}

				int index = rand.nextInt(randomList.size());
				int[] ind = new int[2];
				ind[0] = i;
				ind[1] = j;
				areas[i][j] = jsonService.getArea(randomList.get(index));
				areas[i][j].setIndex(ind);
				areas[i][j].setHighlight(false);
				areas[i][j].setX(i * 30+150);
				areas[i][j].setY(j * 30+30);
			}
		}
		
		int xCity = rand.nextInt(WIDTH - 20);
		int yCity = rand.nextInt(HEIGHT - 20);
		prepareBuildingsMap(xCity, yCity);
	}
	
	private void prepareBuildingsMap(int x, int y){
		buildings = new HashMap<Area, Building>();
		Warehouse warehouse = new Warehouse();
		Area area = areas[x][y];
		area.setBuildingType(BuildingType.WAREHOUSE);
		area.setCost(1);
		this.buildings.put(area, warehouse);
	}

	public void draw(Graphics g) {
		for(Area[] areaRow: areas){
			for(Area area: areaRow){
//				switch (area.getAreaVisible()) {
//				case VISIBLE:
//					g.drawImage(imageManager.getAreaImage(area.getImageNo()), area.getX(), area.getY(), null);
//					break;
//				case NOT_VISIBLE:
//					g.drawImage(imageManager.getAreaImage(area.getImageNo()), area.getX(), area.getY(), null);
//					g.setColor(imageManager.getNotVisible());
//					g.fillRect(area.getX(), area.getY(), 30, 30);
//					break;
//				case NOT_VISITED:
//					g.drawImage(imageManager.getAreaImage(5), area.getX(), area.getY(), null);
//					break;
//				default:
//					break;
//				}
				g.drawImage(imageManager.getAreaImage(area.getImageNo()), area.getX(), area.getY(), null);
				
				if(area.isHighlight()){
					g.setColor(Color.WHITE);
		    		g.drawRect(area.getX()+1, area.getY()+1, 28, 28);
				}
				
				if(area.getFood()!=null){
					g.setColor(Color.BLACK);
					g.fillRect(area.getX()+7, area.getY()+7, 15, 15);
				}
			}
		}
	}

	public Area[][] getAreas() {
		return areas;
	}

	public void setAreas(Area[][] areas) {
		this.areas = areas;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public java.util.Map<Area, Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(java.util.Map<Area, Building> buildings) {
		this.buildings = buildings;
	}
	
}
