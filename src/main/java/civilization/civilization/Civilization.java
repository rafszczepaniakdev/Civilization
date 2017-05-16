package civilization.civilization;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import civilization.human.Human;
import civilization.human.Scout;
import civilization.image.ImageManager;

public class Civilization {
	private City city;
	private List<Human> people;
	private List<Human> deadPeople;
	private ImageManager imageManager;
	
	public Civilization(){
		city = City.getInstance();
		people = new ArrayList<Human>();
		deadPeople = new ArrayList<Human>();
		imageManager = ImageManager.getInstance();
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<Human> getPeople() {
		return people;
	}

	public void setPeople(List<Human> people) {
		this.people = people;
	}
	
	public void addHuman(Human human){
		people.add(human);
	}

	public void draw(Graphics g){
		drawMain(g);
		drawLeftPanel(g);
		drawTopPanel(g);
		drawDeadPeoples(g);
	}
	
	public void drawLeftPanel(Graphics g){
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 150, 600);
		
		g.drawImage(imageManager.getPanel(), 0, 0, null);
		
		int y = 100;
		for(Human ob: people){
			ob.drawAtPanel(g,y);
			y+=30;
		}
	}
	
	public void drawTopPanel(Graphics g){
		g.setColor(Color.WHITE);
		g.drawImage(imageManager.getFruit(), 200, 5, null);
		g.drawString(String.valueOf(city.getFood()), 220, 18);
		g.drawImage(imageManager.getWood(), 390, -1, null);
		g.drawString(String.valueOf(city.getWood()), 420, 18);
//		g.drawString("Stone: "+city.getStone(), 620, 20);
	}

	public void drawMain(Graphics g){
		people.forEach(x->x.draw(g));
	}
	
	public void drawDeadPeoples(Graphics g){
		deadPeople.forEach(x->x.drawTombstone(g));
	}
	
	public void update(){
//		setNotVisible();
		people.forEach(Human::update);
		people.forEach(x->{
			if(x.getHp()<=0){
				if(x instanceof Scout){
					deadPeople.add(new Scout(x.getArea(), x.getId()));
				}
			}
		});
		people.removeIf(x->x.getHp()<=0);
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
