package data_structures;

/**
 * A general-purpose, index-based list contract.
 * Null elements are not permitted.
 *
 * @param <E> the type of elements held in this list
 */
public interface MyList<E> {

    /**
     * Appends the element to the end of the list.
     *
     * @param element the element to add
     */
    void add(E element);

    /**
     * Inserts the element at the specified position, shifting the element
     * currently at that position (if any) and all subsequent elements toward the end.
     *
     * @param index   the position to insert at, from 0 to the list size inclusive
     * @param element the element to add
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index > size}
     */
    void add(int index, E element);

    /**
     * Replaces the element at the specified position with the given one.
     * The size does not change.
     *
     * @param index   the position of the element to replace
     * @param element the new value for that position
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >= size}
     */
    void set(int index, E element);

    /**
     * Removes and returns the element at the specified position,
     * shifting all subsequent elements toward the beginning.
     *
     * @param index the position of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >= size}
     */
    E remove(int index);

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements
     */
    int size();

    /**
     * Returns the index of the first occurrence of the given element,
     * compared using {@code equals}, or -1 if the list does not contain it.
     * @throws IllegalStateException if list is empty
     * @param element the element to search for
     * @return the index of the first occurrence, or -1 if not found
     */
    int indexOf(E element);

    /**
     * Removes all elements from the list. After this call the list is empty.
     */
    void clear();

    /**
     * Returns {@code true} if the list contains no elements.
     *
     * @return {@code true} if the list is empty
     */
    boolean isEmpty();

    /**
     * Returns the element at the specified position.
     *
     * @param index the position of the element to return
     * @return the element at the given position
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index >= size}
     */
    E get(int index);
}