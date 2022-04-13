package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import java.util.Comparator;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.io.File;
import java.util.Map;
import java.util.PriorityQueue;
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
	
//	/*
//	 * Find shortest path from A to B using Dijkstra's algorithm. 
//	 * If there are several solutions, doesn't make any promise about which one it will pick. 
//	 */
//	public MapPath shortestPath(String startPoint, String endPoint) {
//		
//		String x = new String(startPoint);
//		String y = new String(endPoint);
//		Set<String> visitedSet = new LinkedHashSet<String>();		
//		PriorityQueue<MapPath> pathQueue = new PriorityQueue<MapPath>(100, new MapPathComparator());
//		
//		// fill the HashMap with the key/val for the start node, with 0 as distance
//		// fill the rest with infinite vlaues 
//		/*
//		 * Pseudocode 
//		 * 
//		 * 
//		 */
//		return new MapPath();
//	}
//	
	
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
