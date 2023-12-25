import java.util.PriorityQueue;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
		implements GraphADT<NodeType, EdgeType> {

	/**
	 * While searching for the shortest path between two nodes, a SearchNode
	 * contains data about one specific path between the start node and another node
	 * in the graph. The final node in this path is stored in it's node field. The
	 * total cost of this path is stored in its cost field. And the predecessor
	 * SearchNode within this path is referened by the predecessor field (this field
	 * is null within the SearchNode containing the starting node in it's node
	 * field).
	 *
	 * SearchNodes are Comparable and are sorted by cost so that the lowest cost
	 * SearchNode has the highest priority within a java.util.PriorityQueue.
	 */
	protected class SearchNode implements Comparable<SearchNode> {
		public Node node;
		public double cost;
		public SearchNode predecessor;

		public SearchNode(Node node, double cost, SearchNode predecessor) {
			this.node = node;
			this.cost = cost;
			this.predecessor = predecessor;
		}

		public int compareTo(SearchNode other) {
			if (cost > other.cost)
				return +1;
			if (cost < other.cost)
				return -1;
			return 0;
		}
	}

	/**
	 * This helper method creates a network of SearchNodes while computing the
	 * shortest path between the provided start and end locations. The SearchNode
	 * that is returned by this method is represents the end of the shortest path
	 * that is found: it's cost is the cost of that shortest path, and the nodes
	 * linked together through predecessor references represent all of the nodes
	 * along that shortest path (ordered from end to start).
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return SearchNode for the final end node within the shortest path
	 * @throws NoSuchElementException when no path from start to end is found or
	 *                                when either start or end data do not
	 *                                correspond to a graph node
	 */
	protected SearchNode computeShortestPath(NodeType start, NodeType end) {

		// check if start or end node does not exist in the path
		if (!containsNode(start) || !containsNode(end)) {
			throw new NoSuchElementException("start or end node does not exist!");
		} // check if the start and end node equals, then return the start node
		else if (start.equals(end)) {
			return new SearchNode(nodes.get(start), 0, null);
		} else {
			// create a priority queue to store search nodes
			PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>();
			// create a hash table to store visited nodes
			Hashtable<NodeType, Node> visited = new Hashtable<NodeType, Node>();
			// initialize the start node of the path
			Node firstNode = nodes.get(start);
			SearchNode startNode = new SearchNode(firstNode, 0, null);
			// add the start node to the priority queue
			pq.add(startNode);

			// the main loop of Dijkstra's algorithm to find the shortest path while
			// priority queue is not empty
			while (!pq.isEmpty()) {
				// remove the current node from the priority queue
				SearchNode currentNode = pq.remove();

				// if the current node is not in the visited hash table, add the node to the
				// hash table
				if (!visited.containsKey(currentNode.node.data)) {
					visited.put(currentNode.node.data, currentNode.node);
				}

				// if the current node equals the end node, return it
				if (currentNode.node.data.equals(end)) {
					return currentNode;
				}

				// traverse the every node with leaving edge and add unvisited node to the
				// priority queue
				for (Edge edge : currentNode.node.edgesLeaving) {

					// initialize the successor node
					Node successorNode = edge.successor;

					// check whether the successor node has been visited before
					if (!visited.containsKey(successorNode.data)) {

						// initialize the new cost
						double newCost = currentNode.cost + edge.data.doubleValue();

						SearchNode nextNode = new SearchNode(successorNode, newCost, currentNode);
						// add the next node to the priority queue
						pq.add(nextNode);
					}
				}

			}
		}
		// when no path from start to end is found
		throw new NoSuchElementException("there is no path from start to end!");
	}

	/**
	 * Returns the list of data values from nodes along the shortest path from the
	 * node with the provided start value through the node with the provided end
	 * value. This list of data values starts with the start value, ends with the
	 * end value, and contains intermediary values in the order they are encountered
	 * while traversing this shorteset path. This method uses Dijkstra's shortest
	 * path algorithm to find this solution.
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return list of data item from node along this shortest path
	 */
	public List<NodeType> shortestPathData(NodeType start, NodeType end) {

		// initialize the end node in the shortest path
		SearchNode endNode = computeShortestPath(start, end);

		// create the list to store the data values from nodes along the shortest path
		List<NodeType> list = new LinkedList<NodeType>();

		// traverse the path backward to add the data value of each node
		while (endNode != null) {
			list.add(0, endNode.node.data);
			// update the end node to its predecessor
			endNode = endNode.predecessor;
		}

		// return the list of data item from node along this shortest path
		return list;
	}

	/**
	 * Returns the cost of the path (sum over edge weights) of the shortest path
	 * freom the node containing the start data to the node containing the end data.
	 * This method uses Dijkstra's shortest path algorithm to find this solution.
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return the cost of the shortest path between these nodes
	 */
	public double shortestPathCost(NodeType start, NodeType end) {

		// the cost of the shortest path obtained from computeShortestPath method
		return computeShortestPath(start, end).cost;
	}

	// TODO: implement 3+ tests in step 8.

	/**
	 * This test method uses an example graph and checks that the results of
	 * implementation match what it should be.
	 * 
	 */
	@Test
	public void test1() {

		// initialize the Dijkstra graph to test
		DijkstraGraph<String, Double> test1 = new DijkstraGraph<>();

		// insert four nodes into the graph
		test1.insertNode("A");
		test1.insertNode("B");
		test1.insertNode("C");
		test1.insertNode("D");

		// insert the edges between nodes
		test1.insertEdge("A", "B", 1.0);
		test1.insertEdge("A", "C", 2.0);
		test1.insertEdge("B", "D", 2.2);
		test1.insertEdge("C", "D", 2.3);

		List<String> pathList1 = new LinkedList<String>();
		pathList1 = test1.shortestPathData("A", "D");

		// check whether the list equals as expected
		assertEquals("[A, B, D]", pathList1.toString());

	}

	/**
	 * This test method uses an same example graph above, but check the cost and sequence
	 * of data along the shortest path between a different start and end node.
	 * 
	 */
	@Test
	public void test2() {

		// initialize the Dijkstra graph to test
		DijkstraGraph<String, Double> test2 = new DijkstraGraph<>();

		// insert four nodes into the graph
		test2.insertNode("A");
		test2.insertNode("B");
		test2.insertNode("C");
		test2.insertNode("D");

		// insert the edges between nodes
		test2.insertEdge("A", "B", 1.0);
		test2.insertEdge("A", "C", 2.0);
		test2.insertEdge("B", "D", 2.2);
		test2.insertEdge("C", "D", 2.3);
		test2.insertEdge("D", "C", 2.3);

		List<String> pathList2 = test2.shortestPathData("B", "C");

		// check whether the list equals as expected
		assertEquals("[B, D, C]", pathList2.toString());

		double pathCost = test2.shortestPathCost("B", "C");

		// check whether the cost equals as expected
		assertEquals(4.5, pathCost);

	}

	/**
	 * This test method checks the behavior of implementation when the node that you are
	 * searching for a path between exist in the graph, but there is no sequence of
	 * directed edges that connects them from the start to the end.
	 */
	@Test
	public void test3() {

		// initialize the Dijkstra graph to test
		DijkstraGraph<String, Double> test3 = new DijkstraGraph<>();

		// insert four nodes into the graph
		test3.insertNode("a");
		test3.insertNode("b");
		test3.insertNode("c");
		test3.insertNode("d");

		// insert the edges between nodes
		test3.insertEdge("a", "b", 1.0);
		test3.insertEdge("b", "d", 2.2);
		test3.insertEdge("c", "d", 2.3);

		// There is no direct edge from "a" to "c"
		assertThrows(NoSuchElementException.class, () -> {
			test3.shortestPathData("a", "c");
		});

	}

}