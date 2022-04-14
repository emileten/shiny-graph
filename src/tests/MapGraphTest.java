package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.MapGraph;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


@TestInstance(Lifecycle.PER_CLASS)
class MapGraphTest {

	public MapGraph thisGraph;
	public MapGraph brokenGraph;
	public String yamlFileString = "/Users/emile/Documents/shiny-graph/src/tests/testMapGraph.yaml";	
	public String yamlFileStringIsolatedNode = "/Users/emile/Documents/shiny-graph/src/tests/testMapGraphBrokenPath.yaml";	
	public List<String> expectedShortestPathSteps = Arrays.asList("Wishka", "Irmoupolis", "Ziouxuan", "Tetrov", "Murat", "Larti");
	public Double expectedShortestPathCost = 33.;
	public String exampleEdgeStartString = "start";
	public String exampleEdgeEndString = "end";
	public String exampleEdgeEndString2 = "other node end";
	public Double exampleEdgeDistanceDouble = 10.;
	public Double exampleEdgeDistanceDouble2 = 20.;
	public MapGraph.MapPath exampleSingleEdgePath;
	public MapGraph.MapPath exampleDoubleEdgePath;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		thisGraph = new MapGraph("/Users/emile/Documents/shiny-graph/src/tests/testMapGraph.yaml");
		brokenGraph = new MapGraph("/Users/emile/Documents/shiny-graph/src/tests/testMapGraphBrokenPath.yaml");	
	}

	@BeforeEach
	void setUpBeforeTests() throws Exception{
		this.exampleSingleEdgePath = new MapGraph.MapPath();
		this.exampleSingleEdgePath.addEdge(new MapGraph.MapEdge(this.exampleEdgeStartString, this.exampleEdgeEndString, this.exampleEdgeDistanceDouble));
		this.exampleDoubleEdgePath = new MapGraph.MapPath(this.exampleSingleEdgePath);
		this.exampleDoubleEdgePath.addEdge(new MapGraph.MapEdge(this.exampleEdgeEndString, this.exampleEdgeEndString2, this.exampleEdgeDistanceDouble2));
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
	void testConstructorIsolatedNode() {	
		assertTrue(brokenGraph.concreteGraphMap.listNodes().contains("Larti"));
		assertTrue(brokenGraph.concreteGraphMap.listChildren("Larti").isEmpty());
		assertTrue(brokenGraph.concreteGraphMap.listParents("Larti").isEmpty());
	}

	@Test
	void testMapEdge() {
		MapGraph.MapEdge thisEdge = new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble);
		assertTrue(thisEdge.getStart().equals(exampleEdgeStartString));
		assertTrue(thisEdge.getEnd().equals(exampleEdgeEndString));
		assertTrue(thisEdge.getDistance().equals(exampleEdgeDistanceDouble));
	}
	
	@Test
	void testMapEdgeEquals() {
		assertEquals(
				new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble),
				new MapGraph.MapEdge(exampleEdgeStartString, exampleEdgeEndString, exampleEdgeDistanceDouble));				
	}
	
	@Test
	void testMapPath() {
		assertTrue(exampleSingleEdgePath.totalDistance().equals(exampleEdgeDistanceDouble));
		assertTrue(exampleSingleEdgePath.startNode().equals(exampleEdgeStartString));
		assertTrue(exampleSingleEdgePath.endNode().equals(exampleEdgeEndString));
		assertTrue(exampleDoubleEdgePath.totalDistance().equals(exampleEdgeDistanceDouble + 20.));
		assertTrue(exampleDoubleEdgePath.endNode().equals(this.exampleEdgeEndString2));
	}
	
	@Test 
	void testMapPathComparator() {
		assertTrue(new MapGraph.MapPathComparator().compare(this.exampleDoubleEdgePath, this.exampleSingleEdgePath)>0);
	}
	
	@Test 
	void testPathSteps() {	
		assertEquals(3, this.exampleDoubleEdgePath.pathSteps().size());
		assertEquals(exampleEdgeStartString,this.exampleDoubleEdgePath.pathSteps().get(0));
		assertEquals(exampleEdgeEndString,this.exampleDoubleEdgePath.pathSteps().get(1));
		assertEquals(exampleEdgeEndString2,this.exampleDoubleEdgePath.pathSteps().get(2));
	}
	
	@Test 
	void testShortestPath() {	
		MapGraph.MapPath exampleShortestPath = this.thisGraph.shortestPath(
				expectedShortestPathSteps.get(0),
				expectedShortestPathSteps.get(expectedShortestPathSteps.size()-1)
				);
		assertEquals(expectedShortestPathSteps, exampleShortestPath.pathSteps());
		assertEquals(expectedShortestPathCost, exampleShortestPath.totalDistance());		
		
	}

	
	@Test 
	void testShortestPathNoSolutionGivesEmptyPath() {	
		MapGraph.MapPath exampleShortestPath = this.brokenGraph.shortestPath(
				expectedShortestPathSteps.get(0),
				expectedShortestPathSteps.get(expectedShortestPathSteps.size()-1)
				);
		assertTrue(exampleShortestPath.isEmpty());		
	}

	
}
