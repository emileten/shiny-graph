package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.MapGraph;
import main.MapGraph.MapPath;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


@TestInstance(Lifecycle.PER_CLASS)
class MapGraphTest {

	public MapGraph thisGraph;
	public String yamlFileString = "/Users/emile/Documents/shiny-graph/src/tests/testMapGraph.yaml";
	public String exampleEdgeStartString = "start";
	public String exampleEdgeEndString = "end";
	public Double exampleEdgeDistanceDouble = 10.;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		thisGraph = new MapGraph("/Users/emile/Documents/shiny-graph/src/tests/testMapGraph.yaml");
	}

	
	@Test
	void testConstructor() {
		
		assertTrue(thisGraph.concreteGraphMap.listNodes().contains("Tetrov"));
		assertTrue(thisGraph.concreteGraphMap.listNodes().contains("Wishka"));
		assertTrue(thisGraph.concreteGraphMap.listChildren("Tetrov").containsKey("Ziouxuan"));
		assertTrue(!thisGraph.concreteGraphMap.listChildren("Tetrov").containsKey("Wishka"));
		assertTrue(thisGraph.concreteGraphMap.listChildren("Tetrov").get("Larti").contains(50.));

	}

	@Test
	void testMapEdge() {
		MapGraph.MapEdge thisEdge = new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble);
		assertTrue(thisEdge.getStart().equals(exampleEdgeStartString));
		assertTrue(thisEdge.getEnd().equals(exampleEdgeEndString));
		assertTrue(thisEdge.getDistance().equals(exampleEdgeDistanceDouble));
	}
	
	@Test
	void testMapPath() {
		MapGraph.MapPath thisPath = new MapGraph.MapPath();
		thisPath.addEdge(new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble));
		assertTrue(thisPath.totalDistance().equals(exampleEdgeDistanceDouble));
		assertTrue(thisPath.startNode().equals(exampleEdgeStartString));
		assertTrue(thisPath.endNode().equals(exampleEdgeEndString));
		thisPath.addEdge(new MapGraph.MapEdge("other node start", "other node end", 20.));
		assertTrue(thisPath.totalDistance().equals(exampleEdgeDistanceDouble + 20.));
		assertTrue(thisPath.startNode().equals(exampleEdgeStartString));
		assertTrue(thisPath.endNode().equals("other node end"));
	}
	
	@Test 
	void testMapPathComparator() {
		MapGraph.MapPath thisPath = new MapGraph.MapPath();
		thisPath.addEdge(new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble));
		MapGraph.MapPath otherPath = new MapGraph.MapPath();
		otherPath.addEdge(new MapGraph.MapEdge("other node start", "other node end", 20.));
		MapGraph.MapPathComparator mapPathComparator = new MapGraph.MapPathComparator();
		assertTrue(mapPathComparator.compare(otherPath, thisPath)>0);
	}
	
//	@Test 
//	void testShortestPath() {
//		
//		/*
//		 * Expects : 
//		 * Wishka - Marman - Panju - Etrios - Larti, cost of 40
//		 * lowest cost path between Wishka and Larti
//		 */
//	}
	// For shortest, might want to draw a graph....

}
