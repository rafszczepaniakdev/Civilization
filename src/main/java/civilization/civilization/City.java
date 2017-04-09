package civilization.civilization;

public class City {
	private static City instance;
	private int wood;
	private int food;
	private int stone;
	
	public static synchronized City getInstance(){
		return instance==null ? instance = new City() : instance;
	}
	
	public City(){
		wood=200;
		food=200;
		stone=200;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}
	
	
}
