import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class contains three test methods that test the implementation of the
 * HashtableMap class to verify the put, remove, and rehashing operations.
 * 
 */
class HashtableMapTest {

	/**
	 * This method focuses on testing put functionality. It adds five new key-value
	 * pairs to this collection and checks whether these five pairs exist.
	 * 
	 */
	@Test
	void test1() {
		HashtableMap test1 = new HashtableMap<>(10);
		test1.put("A", 1);
		test1.put("B", 2);
		test1.put("C", 3);
		test1.put("D", 4);
		test1.put("E", 5);

		assertEquals(true, test1.containsKey("A"));
		assertEquals(true, test1.containsKey("B"));
		assertEquals(true, test1.containsKey("C"));
		assertEquals(true, test1.containsKey("D"));
		assertEquals(true, test1.containsKey("E"));
	}

	/**
	 * This method focuses on testing remove functionality. It adds two new
	 * key-value pairs to this collection, removes one pair, and checks whether the
	 * removed pair does not exist in the collection anymore.
	 * 
	 */
	@Test
	void test2() {
		HashtableMap test2 = new HashtableMap<>(10);

		test2.put("a", 10);
		test2.put("b", 11);

		test2.remove("a");

		assertEquals(false, test2.containsKey("a"));
		assertEquals(true, test2.containsKey("b"));
	}

	/**
	 * This method focuses on testing rehashing functionality. It adds seven new
	 * key-value pairs to this collection, whose capacity is 10 (the load factor
	 * becomes greater than or equal to 70%.) Then, checks whether rehash method
	 * worked and the capacity of the hash table doubled.
	 * 
	 */
	@Test
	void test3() {
		HashtableMap test3 = new HashtableMap<>(10);

		test3.put("aa", 11);
		test3.put("bb", 22);
		test3.put("cc", 33);
		test3.put("dd", 44);
		test3.put("ee", 55);
		test3.put("ff", 66);
		test3.put("gg", 77);

		assertEquals(20, test3.getCapacity());
	}

}
