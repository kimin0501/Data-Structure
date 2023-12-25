import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class contains three test methods that test the implementation of the
 * RedBlackTree class to verify the insertion, rotation, and traversal
 * operations.
 * 
 */
public class RedBlackTreeTester {

	/**
	 * This method inserts a set of integer nodes [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	 * into the Red-Black Tree and checks the results of its in-order and
	 * level-order traversals comes out as expected
	 */
	@Test
	public void firstTest() {

		RedBlackTree<Integer> tree1 = new RedBlackTree<Integer>();

		Integer[] newNodes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		for (Integer insertNode : newNodes) {
			tree1.insert(insertNode);
		}

		assertEquals("[ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]", tree1.toInOrderString());
		assertEquals("[ 4, 2, 6, 1, 3, 5, 8, 7, 9, 10 ]", tree1.toLevelOrderString());
	}

	/**
	 * This method inserts a set of integer nodes [1, 5, 10] into the Red-Black Tree
	 * and checks the results of its in-order and level-order traversals comes out
	 * as expected
	 */
	@Test
	public void secondTest() {

		RedBlackTree<Integer> tree2 = new RedBlackTree<Integer>();

		Integer[] newNodes = { 1, 5, 10 };

		for (Integer insertNode : newNodes) {
			tree2.insert(insertNode);
		}

		assertEquals("[ 1, 5, 10 ]", tree2.toInOrderString());
		assertEquals("[ 5, 1, 10 ]", tree2.toLevelOrderString());

	}

	/**
	 * This method inserts a set of integer nodes [22, 4, 13, 8, 7, 32, 16]
	 * into the Red-Black Tree and checks the results of its in-order and
	 * level-order traversals comes out as expected
	 */
	@Test
	public void thirdTest() {

		RedBlackTree<Integer> tree3 = new RedBlackTree<Integer>();

		Integer[] newNodes = { 22, 4, 13, 8, 7, 32, 16 };

		for (Integer insertNode : newNodes) {
			tree3.insert(insertNode);
		}

		assertEquals("[ 4, 7, 8, 13, 16, 22, 32 ]", tree3.toInOrderString());
		assertEquals("[ 13, 7, 22, 4, 8, 16, 32 ]", tree3.toLevelOrderString());

	}

}
