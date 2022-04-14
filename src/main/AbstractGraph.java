package main;


import java.util.Set;
import java.util.Map;

/*
 * An AbstractGraph is a collection of nodes and edges. Nodes and edges are abstractions that are useful to model
 * various real world phenomena (cities with flight connections, social network users, etc...), and may be represented 
 * (at the software level) in different ways. 
 * 
 *
 * An edge connects two nodes n1 and n2. Let that edge be written <N1, N2>. An edge is 'directed' : it starts from n1, 
 * called the parent node of the edge, and ends in n2, called the child node of the edge. 
 * 
 * An edge can connect a given node to itself, e.g. <N1, N1>. 
 * 
 * n1 is called the parent node of N2 and and, equivalently, N2 is called the child node of N1. A node may have any
 * number of children or parent nodes (it may also not be connected to any edge), 
 * therefore an AbstractGraph may have any number of nodes or edges. 
 * 
 * A path is a sequence of edges where an edge to some node is immediately followed by an edge starting from that node. It is written
 * <<N1, N2>, <N2, N3>.
 * 
 * An edge is labeled -- the label may be of any type -- so we can expand the notation above and write an arbitrary edge as <N1, N2, l> where l is the label of that edge. 
 * A node is associated with its own label, of any type, that uniquely identifies it.
 * 
 * 
 * Specification fields:
 * @spec.specfield : nodes // the set of node identifiers in this graph : <N1, N2, N3 ...>
 * @spec.specfield : edges // the set of edges in this graph. E : <N1, N2, l> ...
 * 
 * Note that the field edges does not merely contain all the data of the nodes field : there may be nodes without
 * any connection.
 * 
 * Abstract invariants :
 * - No equal edge in the graph. Equality is defined as :
 * let E1 : <N1, N2, l> and E2 : <N3, N4, k> then E1 == E2 => N1 == N3, N2==N4. l==k
 * - no duplicates in the collection of node identifiers.
 * - for any N in the nodes field, nodes.N != null and for any edge E in the edges field, E.l != null
 * 
 */
public interface AbstractGraph<K,V> {

	/*
	 * notations : g corresponds to this graph.
	 */
	
	
	/**
	 * mutators 
	 */
	
	
	/*
	 * @param node the node to add to g.
	 * 
	 * @spec.requires g does not contain node, and node != null
	 * @spec.modifies g.nodes 
	 * @spec.effects adds node to g.nodes
	 * 
	 * @throws IllegalArgumentException if node already in g.nodes or node == null. 
	 *
	 */
	void addNode(K node);
	
	/*
	 * @param node node to remove from g. 
	 * 
	 * @spec.modifies g.nodes and g.edges
	 * @spec.effects removes node from g.nodes and all associated edges from g.edges 
	 * 
	 * @throws IllegalArgumentException if node not in g.nodes. 
	 * 
	 */
	void removeNode(K node);
	
	/*
	 * @param child the child node of the edge to add
	 * @param parent the parent node of the edge to add
	 * @label the label of the edge to add
	 * 
	 * @spec.requires child != null, parent != null, label != null, and the edge defined by <child, parent, label> is 
	 * not already contained in g. 
	 * @spec.modifies g.edges and may modify g.nodes
	 * @spec.effects adds the defined edge to g.edges and if any of child or parent are not in g.nodes, adds those to g.nodes.
	 * 
	 * @throws IllegalArgumentException if either of child, parent or label is null, or the edge defined by <child, parent, label>
	 * is already contained in g. 
	 */
	void addEdge(K child, K parent, V label);
	
	/*
	 * @param child the child node of the edge to remove
	 * @param parent the parent node of the edge to remove
	 * @param label the label of the edge to remove
	 * 
	 * @spec.modifies g.edges 
	 * @spec.effects removes the edge from g.edges
	 * 
	 * @throws IllegalArgumentException if the edge is not contained in g. 
	 */
	void removeEdge(K child, K parent, V label);
	
		
	
	/*
	 * observers
	 */
	
	
	
	/*
	 * @return g.nodes
	 */
	Set<K> listNodes();
	
	/*
	 * @param parent the parent node of which to fetch the children node
	 * @return a mapping from children to labels of the edges connected to that parent, without 
	 * any empty values in the mapping. 
	 * 
	 * @throws IllegalArgumentException if parent does not belong to g.nodes. 
	 */
	Map<K, Set<V>> listChildren(K parent);

	/*
	 * @param child the child node of which to fetch the parent nodes
	 * @return a mapping from parents to labels of the edges connected to that children, without 
	 * any empty values in the mapping. 
	 * 
	 * @throws IllegalArgumentException if child does not belong to g.nodes. 
	 */
	Map<K, Set<V>> listParents(K child);
		
	/*
	 * @param child the child node of the edge from which to fetch the label
	 * @param parent the parent node of the edge from which to fetch the label
	 * @return the set of labels associated with the specified edge, if there are such edges. It may be empty. 
	 * 
	 * @throws IllegalArgumentException if g.nodes does not contain parent, or it does but there aren't any edges associating 
	 * parent and child.
	 */
	Set<V> getEdgeLabels(K child, K parent);
}
