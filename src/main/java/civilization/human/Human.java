package civilization.human;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import civilization.civilization.City;
import civilization.civilization.building.BuildingType;
import civilization.image.ImageManager;
import civilization.map.Area;

public abstract class Human {
	protected int id;
	protected int hp;
	protected int stomach;
	protected int capacity;
	protected List<Area> path;
	protected int walkSpeed;
	protected ImageManager imageManager;
	protected Area area;
	protected boolean active;
	protected int actionTimer;
	protected HumanAction action;
	protected City city;
	
	private int liveTimer;
	
	protected Color darkRed = new Color(148,24,24,255);
	protected Color red = new Color(255,0,0,255);
	protected Color darkYellow = new Color(162,107,2,255);
	protected Color yellow = new Color(255,168,0,255);
	protected Color darkBlue = new Color(0,90,183,255);
	protected Color blue = new Color(38,145,255,255);
	
	public Human() {
		super();
		active = false;
		imageManager = ImageManager.getInstance();
		path = new ArrayList<Area>();
		actionTimer=0;
		action = HumanAction.NONE;
		city = City.getInstance();
		liveTimer = 0;
	}
	
	public void drawAtPanel(Graphics g, int y) {
		if(active){
			g.setColor(Color.GREEN);
		}else{
			g.setColor(Color.WHITE);
		}
		g.drawString("#"+id, 10, y);
		
		g.drawString(action.toString(), 75, y);
		
		g.setColor(darkRed);
		g.fillRect(10, y+3, 100, 5);
		g.setColor(red);
		g.fillRect(10, y+3, hp, 5);
		
		g.setColor(darkYellow);
		g.fillRect(10, y+8, 100, 5);
		g.setColor(yellow);
		g.fillRect(10, y+8, stomach, 5);
		
		g.setColor(darkBlue);
		g.fillRect(10, y+13, 100, 5);
		g.setColor(blue);
		g.fillRect(10, y+13, capacity, 5);
	};
	
	public void draw(Graphics g){
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getStomach() {
		return stomach;
	}

	public void setStomach(int stomach) {
		this.stomach = stomach;
	}

	public List<Area> getPath() {
		return path;
	}

	public void setPath(List<Area> path) {
		this.path = path;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getActionTimer() {
		return actionTimer;
	}

	public void setActionTimer(int actionTimer) {
		this.actionTimer = actionTimer;
	};
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HumanAction getAction() {
		return action;
	}

	public void setAction(HumanAction action) {
		this.action = action;
	}

	public void go(){
		if(actionTimer>50){
			if(!path.isEmpty()){
				area = path.remove(0);
				actionTimer = 0;
			}else{
				action=HumanAction.NONE;
			}
		}else{
			actionTimer++;
		}
	}
	
	public void update(){
		live();
		switch (action) {
		case GO:
			if(!path.isEmpty()){
				go();
			}else{
				action=HumanAction.NONE;
			}
			break;
		case COLLECT:
			collectFood();
			break;
		case NONE:
			areaAction();
			break;
		default:
			break;
		}
	}
	
	public void areaAction(){
		if(area.getFood()!=null && capacity<100){
			action = HumanAction.COLLECT;
			actionTimer=0;
			return;
		}
		if(area.getBuildingType()!=null && area.getBuildingType().equals(BuildingType.WAREHOUSE)){
			city.setFood(city.getFood()+capacity);
			capacity=0;
			eat();
			return;
		}
	}
	
	public void collectFood(){
	}
	
	private void live(){
		if(liveTimer>50){
			if(stomach>0){
				stomach-=1;
				liveTimer = 0;
			}else{
				stomach = 0;
				hp -= 2;
				liveTimer = 0;
			}
		}else{
			liveTimer++;
		}
	}
	
	private void eat(){
		if(stomach<100){
			if(actionTimer>50){
				int cityCap = city.getFood();
				if(cityCap>5){
					if(stomach+5>100){
						stomach=100;
					}else{
						stomach+=5;
					}
					cityCap-=2;
					city.setFood(cityCap);
				}else{
					stomach+=cityCap;
					city.setFood(0);
				}
				actionTimer=0;
			}else{
				actionTimer++;
			}
		}
	}
}
