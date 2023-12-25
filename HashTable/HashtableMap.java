import java.util.NoSuchElementException;

/**
 * 
 * @author Ki Min Kang
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

	private int capacity; // the capacity of the hash table
	private int size = 0; // the number of buckets with data in the hash table
	private double loadFactor; // load factor of the hash table (size / capacity)
	private HashPairHelper<KeyType, ValueType>[] hashArray; // array of the hash table

	/**
	 * The helper class pairs key and value to a single mapping object. The class
	 * also contains key and value getter methods respectively.
	 *
	 * @param <KeyType>
	 * @param <ValueType>
	 */
	protected class HashPairHelper<KeyType, ValueType> {

		protected KeyType key; // key of the hash object
		protected ValueType value; // value of the hash object
		protected boolean removed; // flag which indicates whether the pair is removed

		/**
		 * Constructor of HashtableHelper class
		 * 
		 * @param key
		 * @param value
		 */
		protected HashPairHelper(KeyType key, ValueType value) {
			this.key = key;
			this.value = value;
			this.removed = false;
		}

		/**
		 * getter method for key
		 * 
		 * @return key
		 */
		protected KeyType getKey() {
			return this.key;
		}

		/**
		 * getter method for value
		 * 
		 * @return value
		 */
		protected ValueType getValue() {
			return this.value;
		}

		/**
		 * checks if the object is removed
		 * 
		 * @return true if the pair is removed, otherwise false
		 */
		protected boolean isRemoved() {
			return this.removed;
		}

		/**
		 * mark the object as removed
		 * 
		 */
		protected void setRemoved() {
			this.removed = true;
		}

		/**
		 * mark the object as not removed
		 * 
		 */
		protected void unsetRemoved() {
			this.removed = false;
		}
	}

	/**
	 * Constructor of HashtableMap class
	 * 
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	public HashtableMap(int capacity) {
		this.capacity = capacity;
		this.loadFactor = ((double) size / (double) capacity);
		hashArray = new HashPairHelper[capacity];
	}

	/**
	 * Default constructor of HashtableMap class
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HashtableMap() {
		this.capacity = 8;
		this.loadFactor = ((double) size / (double) capacity);
		hashArray = new HashPairHelper[capacity];
	}

	/**
	 * The method adds a new key-value pair/mapping to this collection
	 * 
	 * @param key   - the key of newly added object
	 * @param value - the value of newly added object
	 * @throws IllegalArgumentException - when key is null or duplicate of one
	 *                                  already stored
	 */
	public void put(KeyType key, ValueType value) throws IllegalArgumentException {

		if (key == null) {
			throw new IllegalArgumentException("key cannot be null!");
		}

		if (containsKey(key)) {
			throw new IllegalArgumentException("key is already stored in hash table!");
		}

		HashPairHelper<KeyType, ValueType> hashObject = new HashPairHelper<>(key, value);

		// Calculate (modulo calculate) the index that the hash object should be in
		int index = Math.abs(key.hashCode()) % capacity;

		// If the calculated index is empty, the hash object is assigned to the index
		if (hashArray[index] == null || hashArray[index].isRemoved()) {
			hashArray[index] = hashObject;
			this.size++; // increments the size

			hashArray[index].unsetRemoved();

			this.loadFactor = ((double) size / (double) capacity); // calculate the new load factor

			// If the load factor becomes greater than or equal to 70%, rehash it
			if (loadFactor >= 0.7) {
				rehashHelper();
			}
		} // If the calculated index is not empty, find the empty index from the next
			// index and assign it to it
		else if (hashArray[index] != null && !hashArray[index].isRemoved()) {
			int emptyIndex;
			emptyIndex = index;

			// traverse the hash array after the index of the given key and find the empty
			// index
			for (int i = 0; i < capacity + 1; i++) {
				if (hashArray[(index + i) % capacity] == null || hashArray[(index + i) % capacity].isRemoved()) {
					emptyIndex = (index + i) % capacity;
					break;
				}
			}

			// put the hash object into the empty index in the hash table
			hashArray[emptyIndex] = hashObject;
			hashArray[emptyIndex].unsetRemoved();
			size++; // increments the size

			this.loadFactor = ((double) size / (double) capacity); // calculate the new load factor

			// If the load factor becomes greater than or equal to 70%, rehash it
			if (loadFactor >= 0.7) {
				rehashHelper();
			}

		}

	}

	/**
	 * The helper method rehash the capacity of the hash table, when the load factor
	 * becomes greater than or equal to 70%
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void rehashHelper() {
		// resize the capacity by double
		int doubleCapacity = 2 * this.capacity;

		// set the new hash table with double capacity
		HashPairHelper<KeyType, ValueType>[] rehashArray = new HashPairHelper[doubleCapacity];
		int emptyIndex;

		for (int i = 0; i < capacity; i++) {
			// Find the index which is not empty
			if (hashArray[i] != null && !hashArray[i].isRemoved()) {
				// Calculate (modulo calculate) the rehash index that the hash object should be
				// in
				int rehashIndex = Math.abs(hashArray[i].getKey().hashCode()) % capacity;

				// If the calculated rehash index is empty, the hash object is assigned to the
				// index
				if (rehashArray[rehashIndex] == null || rehashArray[rehashIndex].isRemoved()) {
					rehashArray[rehashIndex] = hashArray[i];
				} // If the calculated rehash index is not empty, find the empty index from the
					// next index and assign it to it
				else if (rehashArray[rehashIndex] != null) {
					emptyIndex = rehashIndex;

					while (rehashArray[emptyIndex % capacity] != null
							&& !rehashArray[emptyIndex % capacity].isRemoved()) {
						emptyIndex++;
					}

					// put the hash object into the empty index in the hash table
					rehashArray[emptyIndex % capacity] = hashArray[i];
					rehashArray[emptyIndex % capacity].unsetRemoved();
				}

				// change the capacity and array with rehash
				this.capacity = doubleCapacity;
				hashArray = rehashArray;
			}
		}

	}

	/**
	 * check whether a key maps to a value within this collection
	 * 
	 * @param key - key of the hash object
	 */
	public boolean containsKey(KeyType key) {

		// traverse the array and whether a key maps to a value within this collection
		for (int i = 0; i < capacity; i++) {
			if (hashArray[i] != null && !hashArray[i].isRemoved() && hashArray[i].getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * retrieve the specific value that a key maps to throws exception when key is
	 * not stored in this collection
	 * 
	 * @param key - key of the hash object
	 * @throws NoSuchElementException - when key is not stored in this collection
	 */
	public ValueType get(KeyType key) throws NoSuchElementException {

		// checks whether the given key is stored in the collection
		if (!containsKey(key)) {
			throw new NoSuchElementException("this key is not stored in this collection");
		}

		ValueType value = null;

		// traverse the array to get the value of the given key
		for (int i = 0; i < capacity; i++) {
			if (hashArray[i] != null && hashArray[i].getKey().equals(key)) {
				value = hashArray[i].getValue();
			}
		}

		return value;
	}

	/**
	 * remove the mapping for a given key from this collection throws exception when
	 * key is not stored in this collection
	 * 
	 * @param key - key of the hash object
	 * @throws NoSuchElementException - when key is not stored in this collection
	 */
	public ValueType remove(KeyType key) throws NoSuchElementException {
		int index = Math.abs(key.hashCode()) % capacity;

		// checks whether the given key is stored in the collection
		if (!containsKey(key)) {
			throw new NoSuchElementException("this key is not stored in this collection");
		}

		int removeIndex = 0;

		// traverse the collection and find the index to remove
		for (int i = 0; i < capacity + 1; i++) {
			if (hashArray[(index + i) % capacity] != null && !hashArray[(index + i) % capacity].isRemoved()
					&& hashArray[(index + i) % capacity].getKey().equals(key)) {
				removeIndex = (index + i) % capacity;
				break;
			}
		}

		ValueType removedValue = hashArray[removeIndex].getValue();

		hashArray[removeIndex].setRemoved();

		return removedValue;
	}

	/**
	 * remove all key-value pairs from this collection
	 */
	public void clear() {
		this.size = 0;
		this.hashArray = new HashPairHelper[capacity];
	}

	/**
	 * retrieve the number of keys stored within this collection
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * retrieve this collection's capacity (size of its underlying array)
	 */
	public int getCapacity() {
		return this.capacity;
	}

}