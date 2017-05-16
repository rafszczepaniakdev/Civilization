package civilization.sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sprite {
	private Map<String, ArrayList> frames; //ACTION_NAME,FRAMES
	
	public Sprite(){
		this.frames = new HashMap<String, ArrayList>();
	}

	public Map<String, ArrayList> getFrames() {
		return frames;
	}

	public void setFrames(Map<String, ArrayList> frames) {
		this.frames = frames;
	}
	
	public void setFrame(String key, ArrayList value){
		this.frames.put(key, value);
	}

}
