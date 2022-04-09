package main;

import java.util.concurrent.ConcurrentHashMap;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class ConcreteGraph implements AbstractGraph {
	
	public static final boolean DEBUG = false;
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String, HashSet<String>>> nodes;
	

	/*
	 * Abstraction function
	 * 
	 * g.nodes corresponds to the keys of this.nodes. 
	 * 
	 * let <N1, N2, l> be an edge linking N1 to N2, labelled l -- it belongs to the abstract field g.edges. 
	 * then this element's label can be found this.nodes['N1']['N2']
	 * 
	 * 
	 * 
	 * Representation invariant : 
	 * 
	 * (1) keys in this.nodes are unique and keys in values of g.nodes are unique. 
	 * (2) all sets contained in this.nodes contain no duplicates. 
	 * (3) keys in this.nodes and values of this.nodes are non null
	 * (4) all sets contained in this.nodes contain no null. 
	 */
	
	
	
	/*
	 * The following representation invariants do not need to be checked : 
	 * (1) Because this.nodes is a Map and values of this.nodes are Maps, keys are unique. 
	 * (2) Because this.nodes values are Sets, they contain no duplicates. 

	 * We are checking (3) and (4) here, as :
	 * - a HashMap does allow for one null key and any number of null values, natively. 
	 * - a HashSet does allow for null values natively. 
	 * 
	 * If not verified, throws a RunTimeException. 
	 */
	private void checkRep() throws RuntimeException {
		
		// TODO consider building such an iterator, you're doing the same thing below.
		for (Map.Entry<String, ConcurrentHashMap<String, HashSet<String>>> edges : this.nodes.entrySet()) {
			if (edges.getKey()==null) {
				throw new RuntimeException("there are null nodes keys");
			}
			if (edges.getValue()==null) {
				throw new RuntimeException("there are null nodes values");
			}
			for (Map.Entry<String, HashSet<String>> labels: edges.getValue().entrySet()) {
				for (String l : labels.getValue()) {
					if (l==null) {
						throw new RuntimeException("there are null edge labels");
					}
				}
			}
		}
	}
	
	public ConcreteGraph() {
		this.nodes = new ConcurrentHashMap<String, ConcurrentHashMap<String,HashSet<String>>>();
	}
	
	@Override
	public void addNode(String node) {
		if (DEBUG) {
			this.checkRep();
		}
		if (this.nodes.containsKey(node)) {
			throw new IllegalArgumentException("the node " + node + " is already present in this graph");
		}
		ConcurrentHashMap<String, HashSet<String>> emptyEdgesMap = new ConcurrentHashMap<>();
		this.nodes.put(node, emptyEdgesMap);
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public void removeNode(String node) {
		if (DEBUG) {
			this.checkRep();
		}
		if (!this.nodes.containsKey(node)) {
			throw new IllegalArgumentException("the node " + node + " is not present in this graph");
		}
		this.nodes.remove(node); // removes all the edges where node is the parent, if any
		// then remove all edges where node is the children, if any ! 
		for (Map.Entry<String, ConcurrentHashMap<String, HashSet<String>>> edges : this.nodes.entrySet()) {
			if (edges.getValue().containsKey(node)) {
				edges.getValue().remove(node);
			}
		}
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public void addEdge(String child, String parent, String label) {
		if (DEBUG) {
			this.checkRep();
		}
		if (child==null) {
			throw new IllegalArgumentException("the child argument should be given a non null value");
		}	
		if (parent==null) {
			throw new IllegalArgumentException("the parent argument should be given a non null value");
		}	
		if (label==null) {
			throw new IllegalArgumentException("the label argument should be given a non null value");
		}
		
		
		if (!this.nodes.containsKey(parent)) {
			this.nodes.put(parent, new ConcurrentHashMap<String, HashSet<String>>());	
		} 
		
		if (!this.nodes.containsKey(child)) {
			this.nodes.put(child, new ConcurrentHashMap<String, HashSet<String>>());	
		} 
		
		if (!this.nodes.get(parent).containsKey(child)) {
			this.nodes.get(parent).put(child, new HashSet<String>());	
		}
		
		if (this.nodes.get(parent).get(child).contains(label)) {
			throw new IllegalArgumentException("the edge defined by <" + parent + ", " + child + ", " + label + "> already exists in the graph");
		}
		// add child 
		this.nodes.get(parent).get(child).add(label);
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public void removeEdge(String child, String parent, String label) {
		if (DEBUG) {
			this.checkRep();
		}
		if (!this.nodes.containsKey(parent)) {
			throw new IllegalArgumentException("the node " + parent + " does not belong to the graph");
		}
		
		if (!this.nodes.get(parent).containsKey(child)) {
			throw new IllegalArgumentException("the node " + child + " is not a child of parent node");
			
		}
		
		if (!this.nodes.get(parent).get(child).contains(label)) {
			throw new IllegalArgumentException("the edge defined by <" + parent + ", " + child + ", " + label + "> does not belong to this graph");
		}
		// what does this do if parent not in nodes or child not in nodes[parent] or label not in nodes[parent][child]?
		this.nodes.get(parent).get(child).remove(label);
		if (this.nodes.get(parent).get(child).isEmpty()) {
			this.nodes.get(parent).remove(child);
		}
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public HashSet<String> listNodes() {
		return new HashSet<String>(this.nodes.keySet());
	}

	@Override
	public HashMap<String, Set<String>> listChildren(String parent) {
		if (DEBUG) {
			checkRep();
		}		
		if (!this.nodes.containsKey(parent)) {
			throw new IllegalArgumentException("the node " + parent + " does not belong to the graph");			
		}
		
		HashMap<String, Set<String>> childrenMapping = new HashMap<String, Set<String>>(this.nodes.get(parent));
		
		for(Map.Entry<String, Set<String>> children : childrenMapping.entrySet()) {
			if (children.getValue().isEmpty()) {
				childrenMapping.remove(children.getKey());
			}
		}
		if (DEBUG) {
			checkRep();
		}		
		return childrenMapping;
	}
	
	

	@Override
	public HashMap<String, Set<String>> listParents(String child) {
		if (DEBUG) {
			checkRep();
		}		
		if (!this.nodes.containsKey(child)) {
			throw new IllegalArgumentException("the node " + child + " does not belong to the graph");			
		}	
		
		HashMap<String, Set<String>> parentsMapping = new HashMap<String, Set<String>>();
		for (Map.Entry<String, ConcurrentHashMap<String, HashSet<String>>> edges : this.nodes.entrySet()) {
			Set<String> parentEdgesLabels = new HashSet<String>();
			for (Map.Entry<String, HashSet<String>> labels: edges.getValue().entrySet()) {
				if (labels.getKey()==child) {
					for (String l : labels.getValue()) {
						parentEdgesLabels.add(l);
					}					
				}
			}
			if (!parentEdgesLabels.isEmpty()) {
				parentsMapping.put(edges.getKey(), parentEdgesLabels);
			}
		}
		if (DEBUG) {
			checkRep();
		}
		return parentsMapping;
	}
	
	@Override
	public HashSet<String> getEdgeLabels(String child, String parent){
		if (DEBUG) {
			checkRep();
		}		
		if (!this.nodes.containsKey(parent)) {
			throw new IllegalArgumentException(parent + " is not a node in this graph");
		}
		
		if (!this.nodes.get(parent).containsKey(child)) {
			throw new IllegalArgumentException(child + " is not a child of " + parent);			
		}
		
		if (DEBUG) {
			checkRep();
		}		
		return new HashSet<String>(this.nodes.get(parent).get(child));
	}

}
