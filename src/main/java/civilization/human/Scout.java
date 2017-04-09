package civilization.human;

import java.awt.Color;
import java.awt.Graphics;

import civilization.map.Area;

public class Scout extends Human {

	public Scout(Area area) {
		super();
		hp = 100;
		stomach = 100;
		walkSpeed = 2;
		super.area = area;
	}

	@Override
	public void drawAtPanel(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("JAKIS SCOUT", 10, 100);
	}
	
	@Override
	public void draw(Graphics g) {
		if(!active)
			g.setColor(Color.pink);
		else
			g.setColor(Color.MAGENTA);
		g.fillOval(area.getX()+5, area.getY()+5, 20, 20);
	}
	
}
