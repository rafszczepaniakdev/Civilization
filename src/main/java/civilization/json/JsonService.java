package civilization.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import civilization.map.Area;
import civilization.map.AreaVisible;

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
//		area.setAreaVisible(AreaVisible.NOT_VISITED);
		return area;
	}

}
