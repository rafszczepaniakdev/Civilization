package civilization.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import civilization.civilization.building.BuildingType;
import civilization.map.Area;
import civilization.map.AreaVisible;
import civilization.map.Food;
import civilization.map.Wood;
import civilization.sprite.Frame;
import civilization.sprite.Sprite;

public class JsonService {
	private static JsonService instance;
	private JSONParser parser;
	private Object obj;

	public JsonService() {
		super();
		this.parser = new JSONParser();
	}

	public static synchronized JsonService getInstance() {
		return instance == null ? instance = new JsonService() : instance;
	}

	public JSONParser getParser() {
		return parser;
	}

	public void setParser(JSONParser parser) {
		this.parser = parser;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Area getArea(int index) {
		try {
			setObj(getParser().parse(new FileReader("src/areas.json")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JSONObject json = (JSONObject) getObj();
		JSONObject areaObject = (JSONObject) json.get("area" + index);
		int type = index;
		int cost = Integer.parseInt(String.valueOf(areaObject.get("cost")));
		int imageUrl = Integer.parseInt(String.valueOf(areaObject.get("imageNo")));
		ArrayList<Integer> availableNeighbors = new ArrayList<Integer>();
		JSONArray array = (JSONArray) areaObject.get("availableNeighbors");
		for(Object object:array){
			availableNeighbors.add(Integer.parseInt(String.valueOf(object)));
		}

		Area area = new Area();
		area.setType(type);
		area.setCost(cost);
		area.setImageNo(imageUrl);
		area.setAvailableNeighbors(availableNeighbors);
		area.setHiglightBuilding(BuildingType.NONE);
		Random rand = new Random();
		if(rand.nextInt(100)<=8 && area.getCost()>0){
			Food food = new Food();
			area.setFood(food);
		}
		if(rand.nextInt(100)<=40  && area.getImageNo()==4){
			Wood wood = new Wood();
			area.setWood(wood);
		}
//		area.setAreaVisible(AreaVisible.NOT_VISITED);
		return area;
	}

	public Map<String, Sprite> getPlayerSprite(){
		try {
			obj = parser.parse(new FileReader("src/sprite.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject json = (JSONObject) obj;
		JSONObject scout = (JSONObject) json.get("scout");
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("down");
		actions.add("up");
		actions.add("left");
		actions.add("right");
		
		Map<String, Sprite> spriteInfo = new HashMap<String, Sprite>();
		Sprite spriteScout = new Sprite();
		
		for(String action: actions){
			ArrayList<Frame> frames = new ArrayList<Frame>();
			JSONObject blueManAction = (JSONObject) scout.get(action);
			for(int j=1; j<=3;j++){
				JSONObject frame = (JSONObject) blueManAction.get("frame"+j);
				int xFrom = Integer.valueOf(frame.get("xFrom").toString());
				int xTo = Integer.valueOf(frame.get("xTo").toString());
				int yFrom = Integer.valueOf(frame.get("yFrom").toString());
				int yTo = Integer.valueOf(frame.get("yTo").toString());
				frames.add(new Frame(xFrom,yFrom,xTo,yTo));
			}
			spriteScout.setFrame(action, frames);
		}
		spriteInfo.put("scout", spriteScout);
	
		return spriteInfo;
	}
	
}
