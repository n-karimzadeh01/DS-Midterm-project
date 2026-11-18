package data_structures;

public class MyNode<K , V> {
    private final K key;
    private V value;
    private MyNode<K , V> next;

    public MyNode(K key, V value){
        this.key = key;
        this.value = value;
        next = null;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNext(MyNode<K, V> next) {
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public MyNode<K, V> getNext() {
        return next;
    }

    public V getValue() {
        return value;
    }
}
