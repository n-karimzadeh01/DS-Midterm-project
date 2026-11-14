package data_structures;

interface Stack<E> {

    // throws IllegalArgumentException if element is null
    // throws IllegalStateException    if the stack is full
    void push(E element);

    //throws IllegalStateException if the stack is empty
    E pop();

    E peek();

    boolean isEmpty();

    void clear();

    int size();
}