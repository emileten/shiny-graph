package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.io.File;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;



/*
 * a kind of ConcreteGraph, with numerical labels representing distances between points in space. 
 * rather than inheritance, we choose composition.  
 */
public class MapGraph {

	// a particular type of ConcreteGraph
	public ConcreteGraph<String, Double> concreteGraphMap;
	
	/*
	 * TODO not sure how this handles empty entries in the yaml (cities not connected to any other city
	 * @param filePath absolute path to yaml file representing this graph
	 */
	public MapGraph(String filePath) {
		
		Yaml yaml = new Yaml();
		this.concreteGraphMap = new ConcreteGraph<String, Double>();		
		try {
			InputStream inputStream = new FileInputStream(new File(filePath));		
			Map<String, Map<String, Double>> obj = yaml.load(inputStream);
			for (Map.Entry<String, Map<String, Double>> node : obj.entrySet()) {	
				if (!this.concreteGraphMap.listNodes().contains(node.getKey())) {
					this.concreteGraphMap.addNode(node.getKey());									
				}
				for (Map.Entry<String, Double> path: node.getValue().entrySet()) {	
					this.concreteGraphMap.addEdge(path.getKey(), node.getKey(), path.getValue());
				}	
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found ! " + filePath);
		}		
		

	}
	
	/*
	 * Find shortest path from A to B using Dijkstra's algorithm. 
	 * If there are several solutions, doesn't make any promise about which one it will pick. 
	 * 
	 * @return MapPath that is empty if there isn't any path leading to destination.
	 */
	public MapPath shortestPath(String startPoint, String endPoint) {
		
		String startNode = new String(startPoint);
		String targetNode = new String(endPoint);
		LinkedHashSet<String> visitedSet = new LinkedHashSet<String>();
		PriorityQueue<MapPath> pathPriorityQueue = new PriorityQueue<MapPath>(100, new MapPathComparator());
		
		
		// initialize : current node is start node, and add a path start -> start (0 distance) to the queue. 
		MapPath startToItselfMapPath = new MapPath();
		startToItselfMapPath.addEdge(new MapEdge(startNode, startNode, 0.));
		pathPriorityQueue.add(startToItselfMapPath);
		
		
		while(!pathPriorityQueue.isEmpty()) {
			
			MapPath bestMapPath = pathPriorityQueue.remove();
			String bestPathDestinationNode = bestMapPath.endNode();
			
			
			if(bestPathDestinationNode.equals(targetNode)) {
				return bestMapPath;
			}
			
			if (visitedSet.contains(bestPathDestinationNode)) {
				continue;
			}
			
			
			for(Map.Entry<String, Set<Double>> childrenEntry: this.concreteGraphMap.listChildren(bestPathDestinationNode).entrySet()) {
				Set<Double> childrenLabels = childrenEntry.getValue();
				if (!childrenLabels.isEmpty()) {
					if (!visitedSet.contains(childrenEntry.getKey())) {
						Double minLabelForChildren = Collections.min(childrenLabels);
						MapPath extendedMapPath = new MapGraph.MapPath(bestMapPath);
						extendedMapPath.addEdge(new MapEdge(bestPathDestinationNode, childrenEntry.getKey(), minLabelForChildren));
						pathPriorityQueue.add(extendedMapPath);							
					}				
				}
			}
			
	
			visitedSet.add(bestPathDestinationNode);
	
			
		}
		
		return new MapGraph.MapPath();

	}
	
	
	/*
	 * represents an edge between two spatial points in the graph, and the distance associated with it. 
	 */
	public static class MapEdge{
		
		private final String start;
		private final String end;
		private final Double distance;
		
		public MapEdge(String start, String end, Double distance) {
			this.start = start;
			this.end = end;
			this.distance = distance;
		}
		
		public String getStart() {
			return new String(this.start);
		}
		
		public String getEnd() {
			return new String(this.end);
		}
		
		public Double getDistance() {
			return Double.valueOf(this.distance);
		}
		
	}
	
	/*
	 * represents a set of edges starting from A and ending in B, in a map represented as a graph. 
	 * It's a path in the sense that it's continuous (one can go through it and arrive at B). 
	 */
	public static class MapPath {
		
		private LinkedHashSet<MapEdge> edges;		
		private Double totalDistance = 0.;
		
		/*
		 * empty set of MapEdge
		 */
		public MapPath() {
			this.edges = new LinkedHashSet<MapEdge>();
		}
		
		/*
		 * copy constructor
		 */
		public MapPath(MapPath otherMapPath) {
			this.edges = new LinkedHashSet<MapEdge>(otherMapPath.edges);
			this.totalDistance = Double.valueOf(otherMapPath.totalDistance());
		}
		
		/*
		 * Add a MapEdge in the set and update the total distance. 
		 */
		public void addEdge(MapEdge edge){
			this.edges.add(edge);
			this.totalDistance = this.totalDistance + edge.getDistance();
		}
		
		/*
		 * @return the starting point. Based on insertion order of edges. 
		 */
		public String startNode() {
			if (!this.edges.isEmpty()) {
				return new ArrayList<MapEdge>(this.edges).get(0).getStart();
			} else {
				throw new RuntimeException("empty path");
			}
		}

		/*
		 * @return the end point. Based on insertion order of edges. 
		 */
		public String endNode() {
			if (!this.edges.isEmpty()) {
				return new ArrayList<MapEdge>(this.edges).get(this.edges.size()-1).getEnd();
			} else {
				throw new RuntimeException("empty path");
			}
		}
		
		/*
		 * @return the total distance
		 */
		public Double totalDistance() {
			if (this.edges.isEmpty()) {
				throw new RuntimeException("empty path");
			} else {
				return Double.valueOf(this.totalDistance);
			}	
		}
		
//		/*
//		 * @return the set of nodes representing the steps of the path
//		 * 
//		 */
//		public HashSet<String> pathSteps(){
//			
//		}
		
	}
	
	/*
	 * Two MapPaths are compared based on their total distances
	 */
	public static class MapPathComparator implements Comparator<MapPath>{
	     public int compare(MapPath path1, MapPath path2) {	    	 	
	         return path1.totalDistance().compareTo(path2.totalDistance());
	     }
	 }
	
	
}
