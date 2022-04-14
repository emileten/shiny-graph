package main;

import java.util.concurrent.ConcurrentHashMap;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class ConcreteGraph<K,V> implements AbstractGraph<K,V> {
	
	public static final boolean DEBUG = false;
	
	private ConcurrentHashMap<K, ConcurrentHashMap<K, HashSet<V>>> nodes;
	

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
		for (Map.Entry<K, ConcurrentHashMap<K, HashSet<V>>> edges : this.nodes.entrySet()) {
			if (edges.getKey()==null) {
				throw new RuntimeException("there are null nodes keys");
			}
			if (edges.getValue()==null) {
				throw new RuntimeException("there are null nodes values");
			}
			for (Map.Entry<K, HashSet<V>> labels: edges.getValue().entrySet()) {
				for (V l : labels.getValue()) {
					if (l==null) {
						throw new RuntimeException("there are null edge labels");
					}
				}
			}
		}
	}
	
	public ConcreteGraph() {
		this.nodes = new ConcurrentHashMap<K, ConcurrentHashMap<K,HashSet<V>>>();
	}
	
	@Override
	public void addNode(K node) {
		if (DEBUG) {
			this.checkRep();
		}
		if (this.nodes.containsKey(node)) {
			throw new IllegalArgumentException("the node " + node.toString() + " is already present in this graph");
		}
		ConcurrentHashMap<K, HashSet<V>> emptyEdgesMap = new ConcurrentHashMap<>();
		this.nodes.put(node, emptyEdgesMap);
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public void removeNode(K node) {
		if (DEBUG) {
			this.checkRep();
		}
		if (!this.nodes.containsKey(node)) {
			throw new IllegalArgumentException("the node " + node.toString() + " is not present in this graph");
		}
		this.nodes.remove(node); // removes all the edges where node is the parent, if any
		// then remove all edges where node is the children, if any ! 
		for (Map.Entry<K, ConcurrentHashMap<K, HashSet<V>>> edges : this.nodes.entrySet()) {
			if (edges.getValue().containsKey(node)) {
				edges.getValue().remove(node);
			}
		}
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public void addEdge(K child, K parent, V label) {
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
			this.nodes.put(parent, new ConcurrentHashMap<K, HashSet<V>>());	
		} 
		
		if (!this.nodes.containsKey(child)) {
			this.nodes.put(child, new ConcurrentHashMap<K, HashSet<V>>());	
		} 
		
		if (!this.nodes.get(parent).containsKey(child)) {
			this.nodes.get(parent).put(child, new HashSet<V>());	
		}
		
		if (this.nodes.get(parent).get(child).contains(label)) {
			throw new IllegalArgumentException("the edge defined by <" + parent.toString() + ", " + child.toString() + ", " + label.toString() + "> already exists in the graph");
		}
		// add child 
		this.nodes.get(parent).get(child).add(label);
		if (DEBUG) {
			this.checkRep();
		}
	}

	@Override
	public void removeEdge(K child, K parent, V label) {
		if (DEBUG) {
			this.checkRep();
		}
		if (!this.nodes.containsKey(parent)) {
			throw new IllegalArgumentException("the node " + parent.toString() + " does not belong to the graph");
		}
		
		if (!this.nodes.get(parent).containsKey(child)) {
			throw new IllegalArgumentException("the node " + child.toString() + " is not a child of parent node");
			
		}
		
		if (!this.nodes.get(parent).get(child).contains(label)) {
			throw new IllegalArgumentException("the edge defined by <" + parent.toString() + ", " + child.toString() + ", " + label.toString() + "> does not belong to this graph");
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
	public HashSet<K> listNodes() {
		return new HashSet<K>(this.nodes.keySet());
	}

	@Override
	public HashMap<K, Set<V>> listChildren(K parent) {
		if (DEBUG) {
			checkRep();
		}		
		if (!this.nodes.containsKey(parent)) {
			throw new IllegalArgumentException("the node " + parent.toString() + " does not belong to the graph");			
		}
		
		HashMap<K, Set<V>> childrenMapping = new HashMap<K, Set<V>>(this.nodes.get(parent));
		
		for(Map.Entry<K, Set<V>> children : childrenMapping.entrySet()) {
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
	public HashMap<K, Set<V>> listParents(K child) {
		if (DEBUG) {
			checkRep();
		}		
		if (!this.nodes.containsKey(child)) {
			throw new IllegalArgumentException("the node " + child.toString() + " does not belong to the graph");			
		}	
		
		HashMap<K, Set<V>> parentsMapping = new HashMap<K, Set<V>>();
		for (Map.Entry<K, ConcurrentHashMap<K, HashSet<V>>> edges : this.nodes.entrySet()) {
			Set<V> parentEdgesLabels = new HashSet<V>();
			for (Map.Entry<K, HashSet<V>> labels: edges.getValue().entrySet()) {
				if (labels.getKey()==child) {
					for (V l : labels.getValue()) {
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
	public HashSet<V> getEdgeLabels(K child, K parent){
		if (DEBUG) {
			checkRep();
		}		
		if (!this.nodes.containsKey(parent)) {
			throw new IllegalArgumentException(parent.toString() + " is not a node in this graph");
		}
		
		if (!this.nodes.get(parent).containsKey(child)) {
			throw new IllegalArgumentException(child.toString() + " is not a child of " + parent.toString());			
		}
		
		if (DEBUG) {
			checkRep();
		}		
		return new HashSet<V>(this.nodes.get(parent).get(child));
	}

}
