package data_structures;
public class MyHashTable<K, V> implements HashTableInterface<K , V> {

    private MyNode<K, V>[] buckets;
    private int size;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 23; //شاید باید اول بگیرمش و بعد هم متود ریسایز رو عوض کنم جوری که اون هم باز عدد اول بده
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        capacity = DEFAULT_CAPACITY;
        buckets = (MyNode<K, V>[]) new MyNode[capacity];
        size = 0;
    }

    /**
     * the goal is to always return a positive index from 0 to capacity
     * TC: O(1) , SC: O(1)
     */
    private int hashToBucket(int hash, int capacity){       //I just implemented the method: Math.floorMod(hash, buckets.length) as a part of DJB2
        return hash - (((int)Math.floor((double) hash /capacity)) *capacity);
    }

    /**
     * DJB2 makes a hash code out of a String, also different hashes for anagrams
     * TC: O(L) L is the length of the key , SC: O(L) just if the key is not already a string (in our case it's always string so O(1))
     */
    private int hash(K key) {                                // DJB2 Method
        if(key == null) throw new IllegalArgumentException("key can not be null");
        String str = key.toString();
        int hash = 5381;
        for(int i= 0; i < str.length(); i++){
            hash = (hash * 33) + str.charAt(i);             // charAt(i) here gives the UTF-16 code of the character
        }
        return hashToBucket(hash, buckets.length);
    }

    // Insert a new key-value pair or update an existing one
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }

        int index = hash(key);
        MyNode<K, V> current = buckets[index];

        //updates the value of an exiting pair
        while (current != null) {
            if (current.getKey().equals(key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }

        //makes a new pair and adds it to the beginning of the chain
        MyNode<K, V> newNode = new MyNode<>(key, value);
        newNode.setNext(buckets[index]);
        buckets[index] = newNode;
        size++;

        if ((double) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
    }

    // Search for a value using its key
    public V get(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        MyNode<K, V> current = buckets[index];

        while (current != null) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
            current = current.getNext();
        }

        return null;
    }

    // copies all values into the given array (its length must be at least size)
    public void copyValuesTo(V[] target) {
        int index = 0;
        for(int i = 0; i < buckets.length; i++){
            MyNode<K, V> current = buckets[i];
            while(current != null){
                target[index] = current.getValue();
                index++;
                current = current.getNext();
            }
        }
    }

    // Check whether a key exists
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }

        int index = hash(key);
        MyNode<K, V> current = buckets[index];

        while (current != null) {
            if (current.getKey().equals(key)) {
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    // Remove an entry using its key
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        MyNode<K, V> current = buckets[index];
        MyNode<K, V> previous = null;

        while (current != null) {
            if (current.getKey().equals(key)) {

                if (previous == null) {
                    buckets[index] = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }

                size--;
                return current.getValue();
            }

            previous = current;
            current = current.getNext();
        }

        return null;
    }

    // Resize the table when it becomes too full
    @SuppressWarnings("unchecked")
    private void resize() {
        MyNode<K, V>[] oldBuckets = buckets;

        buckets = (MyNode<K, V>[]) new MyNode[capacity *2];
        size = 0;

        for (MyNode<K, V> head : oldBuckets) {
            MyNode<K, V> current = head;

            while (current != null) {
                put(current.getKey(), current.getValue());
                current = current.getNext();
            }
        }
        capacity = capacity*2;
    }

    public int getCapacity() {
        return capacity;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
