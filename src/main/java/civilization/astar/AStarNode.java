package civilization.astar;

import civilization.map.Area;

public class AStarNode implements Comparable<AStarNode> {
	private AStarNode parent;
	private Area area;
	private int distanceFromStart;
	private int distanceToFinish;
	private int cost;
	private int x;
	private int y;

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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public AStarNode getParent() {
		return parent;
	}

	public void setParent(AStarNode parent) {
		this.parent = parent;
	}

	public int getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(int distanceFromStart) {
		this.cost -= this.distanceFromStart;
		this.distanceFromStart = distanceFromStart;
		this.cost += this.distanceFromStart;
	}

	public int getDistanceToFinish() {
		return distanceToFinish;
	}

	public void setDistanceToFinish(int distanceToFinish) {
		this.cost -= this.distanceToFinish;
		this.distanceToFinish = distanceToFinish;
		this.cost += this.distanceToFinish;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return "[ FromStart: "+ this.distanceFromStart +" ToFinish: "+ this.distanceToFinish + " Cost: "+this.cost + " AreaState: "+ this.area.getState() + " X,Y :" +this.area.getX() +":"+this.area.getY() + " ]\n";
	}

	@Override
	public int compareTo(AStarNode o) {
		if(o.getArea()!=null && o.getArea().getState().equals(AStarState.FINISH))
			return 1;
		if(this.getArea().getState().equals(AStarState.FINISH))
			return -1;
		
		return this.cost-o.cost<=0 ? -1 : 1;
//		if(this.cost-o.cost<=0){
//				return -1;
//		}else{
//			return 1;
//		}
	}
}
