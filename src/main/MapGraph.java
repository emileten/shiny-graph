package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * a kind of ConcreteGraph, with numerical labels representing distances between points in space. 
 * rather than inheritance, we choose composition.  
 */
public class MapGraph {

	// a particular type of ConcreteGraph
	public ConcreteGraph<String, Double> weightedGraph;
	
	/*
	 * @param filePath absolute path to yaml file representing this graph
	 */
	public MapGraph(String filePath) {
		this.weightedGraph = new ConcreteGraph<String, Double>();
		// file with yaml info 
		// first add cities
		// then add nodes! 
	}
	
	/*
	 * Find shortest path from A to B using Dijkstra's algorithm. 
	 */
	public MapPath shortestPath(String startPoint, String endPoint) {
		return new MapPath();
	}
	
	
	/*
	 * represents an edge between two spatial points in the graph, and the distance associated with it. 
	 */
	public class MapEdge{
		
		public String start;
		public String end;
		public Double distance;
		
		public MapEdge(String start, String end, Double distance) {
			this.start = start;
			this.end = end;
			this.distance = distance;
		}
		
	}
	
	/*
	 * represents a set of edges starting from A and ending in B, in a map represented as a graph. 
	 * It's a path in the sense that it's continuous (one can go through it and arrive at B). 
	 */
	public class MapPath {
		
		private Set<MapEdge> edges;
		private Double totalDistance = 0.;
		
		/*
		 * empty set of MapEdge
		 */
		public MapPath() {
			this.edges = new HashSet<MapEdge>();
		}
		
		/*
		 * Add a MapEdge in the set and update the total distance. 
		 */
		public void addEdge(MapEdge edge){
			this.edges.add(edge);
			this.totalDistance = this.totalDistance + edge.distance;
		}
		
		/*
		 * @return the starting point
		 */
		public String startNode() {
			if (!this.edges.isEmpty()) {
				return new ArrayList<MapEdge>(this.edges).get(0).start;
			} else {
				throw new RuntimeException("empty path");
			}
		}

		/*
		 * @return the end point. 
		 */
		public String endNode() {
			if (!this.edges.isEmpty()) {
				return new ArrayList<MapEdge>(this.edges).get(this.edges.size()-1).start;
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
}
