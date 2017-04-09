package civilization.map;

import java.util.ArrayList;

import civilization.astar.AStarState;
import civilization.civilization.building.BuildingType;

public class Area {
	private int x;
	private int y;
	private int[] index;
	private int type;
	private int cost;
	private boolean highlight;
	private int imageNo;
	private ArrayList<Integer> availableNeighbors;
	private BuildingType buildingType;
	private AStarState state;
	private AreaVisible areaVisible;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getImageNo() {
		return imageNo;
	}

	public void setImageNo(int imageNo) {
		this.imageNo = imageNo;
	}

	public ArrayList<Integer> getAvailableNeighbors() {
		return availableNeighbors;
	}

	public void setAvailableNeighbors(ArrayList<Integer> availableNeighbors) {
		this.availableNeighbors = availableNeighbors;
	}

	public int[] getIndex() {
		return index;
	}

	public void setIndex(int[] index) {
		this.index = index;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public BuildingType getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
		switch (buildingType) {
			case WAREHOUSE:
				this.imageNo = 200;
				break;
			default:
				break;
		}
	}

	public AStarState getState() {
		return state;
	}

	public void setState(AStarState state) {
		this.state = state;
	}

	public AreaVisible getAreaVisible() {
		return areaVisible;
	}

	public void setAreaVisible(AreaVisible areaVisible) {
		this.areaVisible = areaVisible;
	}
	
	

}