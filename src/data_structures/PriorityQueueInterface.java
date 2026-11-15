
package data_structures;

public interface PriorityQueueInterface<T> {

    void enqueue(T element);

    T dequeue();

    T peek();

    int size();

    boolean isEmpty();
}
