package tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.MapGraph;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


@TestInstance(Lifecycle.PER_CLASS)
class MapGraphTest {

	public MapGraph thisGraph;
	public String yamlFileString = "/Users/emile/Documents/shiny-graph/src/tests/testMapGraph.yaml";
	public String exampleEdgeStartString = "start";
	public String exampleEdgeEndString = "end";
	public String exampleEdgeStartString2 = "other node start";
	public String exampleEdgeEndString2 = "other node end";
	public Double exampleEdgeDistanceDouble = 10.;
	public Double exampleEdgeDistanceDouble2 = 20.;
	public MapGraph.MapPath exampleSingleEdgePath;
	public MapGraph.MapPath exampleDoubleEdgePath;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		thisGraph = new MapGraph("/Users/emile/Documents/shiny-graph/src/tests/testMapGraph.yaml");
		
	}

	@BeforeEach
	void setUpBeforeTests() throws Exception{
		this.exampleSingleEdgePath = new MapGraph.MapPath();
		this.exampleSingleEdgePath.addEdge(new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble));
		this.exampleDoubleEdgePath = new MapGraph.MapPath(this.exampleSingleEdgePath);
		this.exampleDoubleEdgePath.addEdge(new MapGraph.MapEdge(this.exampleEdgeStartString2, this.exampleEdgeEndString2, this.exampleEdgeDistanceDouble2));
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
		assertTrue(exampleSingleEdgePath.totalDistance().equals(exampleEdgeDistanceDouble));
		assertTrue(exampleSingleEdgePath.startNode().equals(exampleEdgeStartString));
		assertTrue(exampleSingleEdgePath.endNode().equals(exampleEdgeEndString));
		assertTrue(exampleDoubleEdgePath.totalDistance().equals(exampleEdgeDistanceDouble + 20.));
		assertTrue(exampleDoubleEdgePath.startNode().equals(exampleEdgeStartString));
		assertTrue(exampleDoubleEdgePath.endNode().equals(this.exampleEdgeEndString2));
	}
	
	@Test 
	void testMapPathComparator() {
		assertTrue(new MapGraph.MapPathComparator().compare(this.exampleDoubleEdgePath, this.exampleSingleEdgePath)>0);
	}
	
//	@Test 
//	void testPathSteps() {
//		HashSet<String> expectedSteps = new HashSet<String>();
//		expectedSteps.add(exampleEdgeStartString);
//		expectedSteps.add(exampleEdgeEndString);
//		expectedSteps.add(exampleEdgeStartString2);
//		expectedSteps.add(exampleEdgeEndString2);
//		assertTrue(this.exampleDoubleEdgePath.pathSteps().containsAll(expectedSteps));
//	}
	
//	@Test 
//	void testShortestPath() {
//		
//		/*
//		 * Expects : 
//		 * Wishka - Irmoupolis - Ziouxuan - Tetrov - Murat - Larti, cost of 33
//		 * lowest cost path between Wishka and Larti
//		 */
//		
//	}
	// For shortest, might want to draw a graph....

}
