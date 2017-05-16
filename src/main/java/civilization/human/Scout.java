package civilization.human;

import java.awt.Graphics;

import civilization.image.ImageManager;
import civilization.json.JsonService;
import civilization.map.Area;
import civilization.map.Food;
import civilization.map.MaterialType;
import civilization.map.Wood;
import civilization.sprite.Frame;
import civilization.sprite.Sprite;

public class Scout extends Human {
	private ImageManager imageManager;
	private JsonService jsonService;
	private Sprite sprite;
	private float frame;

	public Scout(Area area, int id) {
		super();
		super.id = id;
		super.hp = 100;
		super.stomach = 100;
		super.walkSpeed = 2;
		super.capacity = 0;
		super.area = area;
		this.jsonService = JsonService.getInstance();
		this.imageManager = ImageManager.getInstance();
		this.sprite = jsonService.getPlayerSprite().get("scout");
		this.frame = 0;
	}

	@Override
	public void draw(Graphics g) {
		if (active || super.action.equals(HumanAction.GO)) {
			int tester = (int) (frame + .5);
			if (tester < 3)
				frame = (float) (frame + .1);
			else
				frame = 0;
		}else{
			frame = 0;
		}
		int xFrom = ((Frame) sprite.getFrames().get("down").get((int) frame)).getxFrom();
		int yFrom = ((Frame) sprite.getFrames().get("down").get((int) frame)).getyFrom();
		int xTo = ((Frame) sprite.getFrames().get("down").get((int) frame)).getxTo();
		int yTo = ((Frame) sprite.getFrames().get("down").get((int) frame)).getyTo();
		int width = xTo - xFrom;
		int height = yTo - yFrom;
		g.drawImage(imageManager.getSprite(), area.getX() - width / 2 +15, area.getY() - height / 2 +10,
				area.getX() + width / 2 +15, area.getY() + height / 2 +10, xFrom, yFrom, xTo, yTo, null);
	}

	@Override
	public void collectFood() {
		if (actionTimer > 50) {
			Food food = area.getFood();
			if (food != null && super.capacity < 100) {
				if (food.getCapacity() >= 2) {
					putToBug(MaterialType.FOOD, 2);
					food.setCapacity(food.getCapacity() - 2);
					if (food.getCapacity() <= 0) {
						area.setFood(null);
					}
				} else {
					putToBug(MaterialType.FOOD, food.getCapacity());
					area.setFood(null);
				}
				actionTimer = 0;
			} else {
				action = HumanAction.NONE;
			}
		} else {
			actionTimer++;
		}
	}

	@Override
	public void collectWood() {
		if (actionTimer > 50) {
			Wood wood = area.getWood();
			if (wood != null && super.capacity < 100) {
				if (wood.getCapacity() >= 2) {
					putToBug(MaterialType.WOOD, 2);
					wood.setCapacity(wood.getCapacity() - 2);
					if (wood.getCapacity() <= 0) {
						area.setWood(null);
						area.setCost(1);
						area.setImageNo(0);
					}
				} else {
					putToBug(MaterialType.WOOD, wood.getCapacity());
					area.setWood(null);
					area.setCost(1);
					area.setImageNo(0);
				}
				actionTimer = 0;
			} else {
				action = HumanAction.NONE;
			}
		} else {
			actionTimer++;
		}
	}
	
	private void putToBug(MaterialType type, int ammount){
		if(bag.get(type)!=null){
			int ammBag = bag.get(type);
			bag.put(type, ammBag+ammount);
		}else{
			bag.put(type, ammount);
		}
		capacity+=ammount;
	}
}
