package civilization.human;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import civilization.image.ImageManager;
import civilization.map.Area;

public abstract class Human {
	protected int hp;
	protected int stomach;
	protected List<Area> path;
	protected int walkSpeed;
	protected ImageManager imageManager;
	protected Area area;
	protected boolean active;
	protected int actionTimer;
	
	public Human() {
		super();
		active = false;
		imageManager = ImageManager.getInstance();
		path = new ArrayList<Area>();
		actionTimer=0;
	}
	
	public void drawAtPanel(Graphics g) {
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
	
	public void go(){
		if(actionTimer>50){
			if(!path.isEmpty()){
				area = path.remove(0);
				actionTimer = 0;
			}
		}else{
			actionTimer++;
		}
	}
	
	public void update(){
		if(!path.isEmpty()){
			go();
		}
		
	}
}
