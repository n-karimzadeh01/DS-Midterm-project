package data_structures;

public interface HashTableInterface<K, V> {

    /**
     * updates the value of an existing key or adds a new key-value pair to the begining of the chain
     * @throws IllegalArgumentException if key or value is null;
     */
    void put(K key, V value);

    V get(K key);

    boolean containsKey(K key);

    V remove(K key);

    int size();

    boolean isEmpty();
}
