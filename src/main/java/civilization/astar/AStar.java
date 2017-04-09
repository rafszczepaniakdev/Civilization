package civilization.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import civilization.map.Area;
import civilization.map.Map;

public class AStar {
	private static AStar instance;
	private Map map;
	
	public AStar() {
		super();
		map = Map.getInstance();
	}

	public static synchronized AStar getInstance() {
		return instance == null ? instance = new AStar() : instance;
	}
	
	public List<Area> generatePath(Area startArea, Area endArea){
		prepareMap();
		startArea.setState(AStarState.START);
		endArea.setState(AStarState.FINISH);
		List<Area> path = new ArrayList<Area>();
		AStarNode startNode = new AStarNode();
		Queue<AStarNode> open = new PriorityQueue<AStarNode>();
		List<AStarNode> close = new ArrayList<AStarNode>();
		
		int[] start = startArea.getIndex();
		int[] end = endArea.getIndex();
		startNode.setX(start[0]);
		startNode.setY(start[1]);
		
		open.addAll(getNeighboorsByAreaIndex(startNode, open, close, start, end));
		
		while(!open.isEmpty()){
			AStarNode actualNode = open.poll();
			
			if(actualNode.getArea().getState().equals(AStarState.FINISH)){
				path = getPath(actualNode);
				open.clear();
			}else{
				close.add(actualNode);
				open.addAll(getNeighboorsByAreaIndex(actualNode, open, close, start, end));
			}
		}
		return path;
	}
	
	private Queue<AStarNode> getNeighboorsByAreaIndex(AStarNode actualNode, Queue<AStarNode> open, List<AStarNode> close, int[] start, int[] end){
		Queue<AStarNode> nodes = new PriorityQueue<AStarNode>();
		Area[][] areas = map.getAreas();
		for(int i=0; i<3; i++){
			for(int j=0;j<3;j++){
				if(i!=1 || j!=1){
					int[] actualIndex = new int[2];
					actualIndex[0] = actualNode.getX()-1+i;
					actualIndex[1] = actualNode.getY()-1+j;
					
					
					if(isIndexInMapArray(actualIndex[0], actualIndex[1]) && !isInQueue(open, actualIndex) && !isInArray(close, actualIndex) 
							&& areas[actualIndex[0]][actualIndex[1]].getCost()>0 && !areas[actualIndex[0]][actualIndex[1]].getState().equals(AStarState.START)){
						Area area = areas[actualIndex[0]][actualIndex[1]];
						AStarNode AStarNode = new AStarNode();
						AStarNode.setArea(area);
						AStarNode.setDistanceFromStart(calculateDistanceFrom(actualNode, start, actualIndex));
						AStarNode.setDistanceToFinish(calculateDistanceFrom(actualNode, end, actualIndex));
						AStarNode.setParent(actualNode);
						AStarNode.setX(actualIndex[0]);
						AStarNode.setY(actualIndex[1]);
						nodes.add(AStarNode);
					}
				}
			}
		}
		return nodes;
	}
	
	private boolean isIndexInMapArray(int x, int y){
		return (x>=0 && x<map.getWIDTH()) && (y>=0 && y<map.getHEIGHT());
	}
	
	private boolean isInArray(List<AStarNode> list, int[] index){
		return list.stream().anyMatch(x -> x.getX()==index[0] && x.getY()==index[1]);
//		for(AStarNode node: list){
//			if(node.getX()==index[0] && node.getY()==index[1]){
//				return true;
//			}
//		}
//		return false;
	}
	
	private boolean isInQueue(Queue<AStarNode> queue, int[] index){
		return queue.stream().anyMatch(x -> x.getX()==index[0] && x.getY()==index[1]);
//		for (AStarNode node : queue) {
//			if(node.getX()==index[0] && node.getY()==index[1]){
//				return true;
//			}
//		}
//		return false;
	}
	
	private int calculateDistanceFrom(AStarNode actualNode, int[] from, int[] actual){
		int distance = 0;
		int x = from[0];
		int y = from[1];
		
		while(x!=actual[0] || y!=actual[1]){
			if(x>actual[0]){
				x--;
			}else if(x<actual[0]){
				x++;
			}
			if(y>actual[1]){
				y--;
			}else if(y<actual[1]){
				y++;
			}
			int cost = map.getAreas()[x][y].getCost();
			if(cost<0)
				cost*=-2;
			distance+=cost;
		}
		
		return distance;
	}
	
	private void prepareMap(){
		Area[][] areas = map.getAreas();
		for(Area[] areaRow: areas){
			for(Area area: areaRow){
				area.setState(AStarState.NORMAL);
			}
		}
	}
	
	private List<Area> getPath(AStarNode actualNode){
		List<Area> path = new ArrayList<Area>();
		AStarNode node = actualNode;
		while(node.getParent()!=null){
			Area area = node.getArea();
			path.add(area);
			node = node.getParent();
		}
		Collections.reverse(path);
		
//		List<Area> result = new ArrayList<Area>();
//		for(int i=path.size()-1;i>=0;i--){
//			result.add(path.get(i));
//		}
		return path;
	}
}
