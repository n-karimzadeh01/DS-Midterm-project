package data_structures;

public class MyStack<E> implements Stack<E> {
    private E[] myStack;
    private int size;

    @SuppressWarnings("unchecked")
    public MyStack(int capacity){
        if(capacity <= 0) throw  new IllegalArgumentException("capacity can not be smaller than 1");
        myStack = (E[]) new Object[capacity];
    }


    @Override
    public void push(E element) {
        if(element ==  null) throw new IllegalArgumentException("null is not accepted");
        if(size == myStack.length) throw new IllegalStateException("stack is full");
        myStack[size] = element;
        size++;
    }

    @Override
    public E pop() {
        if(isEmpty()) throw new IllegalStateException("stack is empty");
        E popped = myStack[size - 1];
        myStack[size - 1] = null;
        size --;
        return popped;
    }

    @Override
    public E peek() {
        if(isEmpty()) throw new IllegalStateException("stack is empty");
        return myStack[size-1];
    }

    @Override
    public boolean isEmpty() {
        return size ==0;
    }

    @Override
    public void clear() {
        while (!isEmpty()){
            pop();
        }
    }

    @Override
    public int size() {
        return size;
    }
}
