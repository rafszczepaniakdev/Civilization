package civilization.civilization;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import civilization.human.Human;
import civilization.map.Area;
import civilization.map.AreaVisible;

public class Civilization {
	private City city;
	private Set<Human> people;
	
	public Civilization(){
		city = City.getInstance();
		people = new HashSet<Human>();
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Set<Human> getPeople() {
		return people;
	}

	public void setPeople(Set<Human> people) {
		this.people = people;
	}
	
	public void addHuman(Human human){
		people.add(human);
	}

	public void draw(Graphics g){
		drawMain(g);
		drawLeftPanel(g);
		drawTopPanel(g);
	}
	
	public void drawLeftPanel(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 150, 600);
		
		for(Human ob: people){
			ob.drawAtPanel(g);
		}
	}
	
	public void drawTopPanel(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(150, 0, 650, 30);
		
		g.setColor(Color.WHITE);
		g.drawString("Food: "+city.getFood(), 220, 20);
		g.drawString("Wood: "+city.getWood(), 420, 20);
		g.drawString("Stone: "+city.getStone(), 620, 20);
	}

	public void drawMain(Graphics g){
		people.forEach(x->x.draw(g));
	}
	
	public void update(){
//		setNotVisible();
		people.forEach(Human::update);
//		updateVisible();
	}
	
//	private void setNotVisible(){
//		people.forEach(x->x.getArea().setAreaVisible(AreaVisible.NOT_VISIBLE));
//	}
//	
//	private void updateVisible(){
//		people.forEach(x->x.getArea().setAreaVisible(AreaVisible.VISIBLE));
//	}
}
