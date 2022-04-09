package tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import main.ConcreteGraph;

@TestInstance(Lifecycle.PER_CLASS)
class ConcreteGraphTest {

	
	public ConcreteGraph thisGraph;
	public String testChildNode; 
	public String testParentNode;
	public String testEdgeLabel;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		testChildNode = "testChildNode";
		testParentNode = "testParentNode";
		testEdgeLabel = "testEdgeLabel";
	}
	
	// uses the constructor
	@BeforeEach
	void setUp() throws Exception {
		thisGraph = new ConcreteGraph();
	}
	

	// addNode + ListNodes
	@Test
	void testAddOneNodeShowedByListNodes() {
		thisGraph.addNode(testChildNode);
		assertTrue(thisGraph.listNodes().contains(testChildNode));
	}
	
	// addNode + RemoveNode + ListNodes
	@Test
	void testAddOneNodeRemoveNodeRemovedFromListNodes() {
		thisGraph.addNode(testChildNode);
		thisGraph.removeNode(testChildNode);
		assertFalse(thisGraph.listNodes().contains(testChildNode));
	}
	
	// addEdge + ListNodes
	@Test 
	void testAddOneEdgeNotExistingNodesAlsoAddsNodes() {
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);
		assertTrue(thisGraph.listNodes().contains(testChildNode));
		assertTrue(thisGraph.listNodes().contains(testParentNode));
	}

	// addEdge + ListChildren + ListParents
	@Test 
	void testAddOneEdgeAddsToListChildrenListParents() {
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);		
		assertTrue(thisGraph.listChildren(testParentNode).containsKey(testChildNode));
		assertTrue(thisGraph.listParents(testChildNode).containsKey(testParentNode));
	}
	
	// addEdge + ListChildren + ListParents
	@Test 
	void testAddOneEdgeDoesNotAddWrongInfoToListChildrenListParents() {
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);
		assertTrue(thisGraph.listChildren(testChildNode).isEmpty());
		assertTrue(thisGraph.listParents(testParentNode).isEmpty());
	}
	
	// addEdge + ListChildren + ListParents
	@Test 
	void testAddOneEdgeAddsLabelInListChildren() {
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);				
		assertTrue(thisGraph.listChildren(testParentNode).get(testChildNode).contains(testEdgeLabel));
	}
	
	
	// addEdge + removeEdge + ListChildren + ListParents
	@Test
	void testRemovesEdgeDoesNotKeepKeys(){
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);
		thisGraph.removeEdge(testChildNode, testParentNode, testEdgeLabel);
		assertFalse(thisGraph.listChildren(testParentNode).containsKey(testChildNode));
		assertFalse(thisGraph.listParents(testChildNode).containsKey(testParentNode));
	}

	// addEdge + removeEdge + listNodes
	@Test
	void testRemovesEdgeKeepsNodes(){
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);
		thisGraph.removeEdge(testChildNode, testParentNode, testEdgeLabel);
		assertTrue(thisGraph.listNodes().contains(testChildNode));
		assertTrue(thisGraph.listNodes().contains(testParentNode));
	}
	
	// getEdgeLabels + addEdge
	@Test
	void testGetEdgeLabelsAddOneEdge() {
		thisGraph.addEdge(testChildNode, testParentNode, testEdgeLabel);
		assertTrue(thisGraph.getEdgeLabels(testChildNode, testParentNode).contains(testEdgeLabel));
	}
	

}
