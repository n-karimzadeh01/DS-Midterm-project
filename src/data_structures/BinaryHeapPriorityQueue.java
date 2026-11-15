
package data_structures;

import java.util.Comparator;

public class BinaryHeapPriorityQueue<T>
        implements PriorityQueueInterface<T> {

    private T[] heap;
    private int size;
    private Comparator<T> comparator;

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public BinaryHeapPriorityQueue(Comparator<T> comparator) {
        this.heap = (T[]) new Object[DEFAULT_CAPACITY];
        this.comparator = comparator;
        this.size = 0;
    }

    @Override
    public void enqueue(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        ensureCapacity();

        heap[size] = element;
        siftUp(size);
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }

        T highestPriority = heap[0];

        size--;
        heap[0] = heap[size];
        heap[size] = null;

        if (!isEmpty()) {
            siftDown(0);
        }

        return highestPriority;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }

        return heap[0];
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (comparator.compare(heap[index], heap[parent]) <= 0) {
                break;
            }

            swap(index, parent);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < size &&
                    comparator.compare(heap[left], heap[largest]) > 0) {
                largest = left;
            }

            if (right < size &&
                    comparator.compare(heap[right], heap[largest]) > 0) {
                largest = right;
            }

            if (largest == index) {
                break;
            }

            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            T[] newHeap = (T[]) new Object[heap.length * 2];

            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
            }

            heap = newHeap;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
