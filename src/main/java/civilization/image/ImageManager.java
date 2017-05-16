package civilization.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

public class ImageManager {
	private static ImageManager instance;
	private Image area0;
	private Image area1;
	private Image area2;
	private Image area3;
	private Image area4;
	private Image area5;
	private Image fruit;
	private Image wood;
	private Image sprite;
	private Image tombstone;
	private Image panel;

	private Color notVisible = new Color(166, 166, 166, 220);
	
	private Image building1;
	
	public ImageManager() {
		super();
		area0 = Toolkit.getDefaultToolkit().getImage("src/images/0.png");
		area1 = Toolkit.getDefaultToolkit().getImage("src/images/1.png");
		area2 = Toolkit.getDefaultToolkit().getImage("src/images/2.png");
		area3 = Toolkit.getDefaultToolkit().getImage("src/images/3.png");
		area4 = Toolkit.getDefaultToolkit().getImage("src/images/4.png");
		area5 = Toolkit.getDefaultToolkit().getImage("src/images/5.png");
		fruit = Toolkit.getDefaultToolkit().getImage("src/images/fruit.png");
		wood = Toolkit.getDefaultToolkit().getImage("src/images/wood.png");
		sprite = Toolkit.getDefaultToolkit().getImage("src/images/sprite.png");
		tombstone = Toolkit.getDefaultToolkit().getImage("src/images/tombstone.png");
		panel = Toolkit.getDefaultToolkit().getImage("src/images/panel.png");
		
		building1 = Toolkit.getDefaultToolkit().getImage("src/images/city.png");
	}

	public static synchronized ImageManager getInstance() {
		if (instance == null)
			instance = new ImageManager();
		return instance;
	}
	
	public Image getAreaImage(int number){
		switch(number){
		case 0:
			return area0;
		case 1:
			return area1;
		case 2:
			return area2;
		case 3:
			return area3;
		case 4:
			return area4;
		case 5:
			return area5;
			
		case 200:
			return building1;
		}
		return null;
	}

	public Color getNotVisible() {
		return notVisible;
	}

	public Image getFruit() {
		return fruit;
	}

	public Image getSprite() {
		return sprite;
	}

	public Image getTombstone() {
		return tombstone;
	}

	public Image getBuilding1() {
		return building1;
	}

	public Image getWood() {
		return wood;
	}

	public Image getPanel() {
		return panel;
	}
	
}
