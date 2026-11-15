package data_structures;
//از تابع هش کد استفاده کردم فعلا.
public class MyHashTable<K, V> implements HashTableInterface<K , V> {

    private Node<K, V>[] buckets;
    private int size;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 16; //شاید باید اول بگیرمش و بعد هم متود ریسایز رو عوض کنم جوری که اون هم باز عدد اول بده
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        capacity = DEFAULT_CAPACITY;
        buckets = (Node<K, V>[]) new Node[capacity];
        size = 0;
    }

    private int hashToBucket(int hash, int capacity){       //I just implemented the method: Math.floorMod(hash, buckets.length) as a part of DJB2
        return hash - (((int)Math.floor((double) hash /capacity)) *capacity);
    }

    private int hash(K key) {                                // DJB2 Method
        String str = key.toString();
        int hash = 5381;
        for(int i= 0; i < str.length(); i++){
            hash = (hash * 33) + str.charAt(i);             // charAt(i) = UTF-16 code of the character
        }
        return hashToBucket(hash, buckets.length);
    }

    // Insert a new key-value pair or update an existing one
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }

        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
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
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    // Check whether a key exists
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }

        int index = hash(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // Remove an entry using its key
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {

                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                return current.value;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    // Resize the table when it becomes too full
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;

        buckets = (Node<K, V>[]) new Node[capacity *2];
        size = 0;

        for (Node<K, V> head : oldBuckets) {
            Node<K, V> current = head;

            while (current != null) {
                put(current.key, current.value);
                current = current.next;
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
