package civilization.human;

import java.awt.Color;
import java.awt.Graphics;

import civilization.map.Area;
import civilization.map.Food;

public class Scout extends Human {

	public Scout(Area area, int id) {
		super();
		super.id = id;
		super.hp = 60;
		super.stomach = 50;
		super.walkSpeed = 2;
		super.capacity = 0;
		super.area = area;
	}

	
	@Override
	public void draw(Graphics g) {
		if(!active)
			g.setColor(Color.pink);
		else
			g.setColor(Color.MAGENTA);
		g.fillOval(area.getX()+5, area.getY()+5, 20, 20);
	}
	
	@Override
	public void collectFood() {
		if(actionTimer>50){
			Food food = area.getFood();
			if(food!=null && super.capacity<100){
				if(food.getCapacity()>=2){
					super.capacity+=2;
					food.setCapacity(food.getCapacity()-2);
					if(food.getCapacity()<=0){
						area.setFood(null);
					}
				}else{
					super.capacity+=food.getCapacity();
					area.setFood(null);
				}
				actionTimer = 0;
			}else{
				action=HumanAction.NONE;
			}
		}else{
			actionTimer++;
		}
	}
	
}
