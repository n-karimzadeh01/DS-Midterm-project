package data_structures;

public class MyArrayList <E> implements MyList<E> {

    private E[] array;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;

    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MyArrayList(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be bigger than 0");
        array = (E[]) new Object[capacity];
    }



    @Override
    public void add(E element) {
        checkBeforeAdd(element);
        array[size] = element;
        size++;
    }

    @Override
    public void add(int index, E element) {
        if(index > size || index <0) throw new IllegalArgumentException("invalid index");
        checkBeforeAdd(element);
        for(int i = size; i > index; i --){
            array[i] = array[i-1];
        }
        array[index] = element;
        size ++;
    }

    @Override
    public void set(int index, E element) {
        boundCheck(index);
        array[index] = element;
    }

    @Override
    public E remove(int index) {
        boundCheck(index);
        E removed = array[index];
        for(int i = index; i < size -1; i++){
            array[index] = array[index + 1];
        }
        array[size] = null;
        size --;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int indexOf(E element) {
        for(int i = 0; i < size; i++){
            if(array[i].equals(element)) return i;
        }
        return -1;
    }

    @Override
    public void clear() {
        for(int i = 0; i<size; i++){
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size) throw new IndexOutOfBoundsException("invalid index");
        return array[index];
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        int newCapacity = size * 2;
        E[] newArr = (E[]) new Object[newCapacity];
        for(int i = 0; i < size ; i++){
            newArr[i] = array[i];
        }
    }

    private void checkBeforeAdd(E element){
        if(element == null) throw new IllegalArgumentException("element can not be  null");
        if(size == capacity) resize();
    }

    private void boundCheck(int index){
        if(index >= size || index<0) throw new IndexOutOfBoundsException();
    }

}