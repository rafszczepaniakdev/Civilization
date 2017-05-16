package civilization.sprite;

public class Frame {
	private int xFrom;
	private int yFrom;
	private int xTo;
	private int yTo;

	public int getxFrom() {
		return xFrom;
	}

	public void setxFrom(int xFrom) {
		this.xFrom = xFrom;
	}

	public int getyFrom() {
		return yFrom;
	}

	public void setyFrom(int yFrom) {
		this.yFrom = yFrom;
	}

	public int getxTo() {
		return xTo;
	}

	public void setxTo(int xTo) {
		this.xTo = xTo;
	}

	public int getyTo() {
		return yTo;
	}

	public void setyTo(int yTo) {
		this.yTo = yTo;
	}

	public Frame(int xFrom, int yFrom, int xTo, int yTo) {
		super();
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
	}

}
