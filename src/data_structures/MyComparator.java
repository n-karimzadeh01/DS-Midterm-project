package data_structures;

public interface MyComparator<T> {
    // negative: a comes before b | zero: equal | positive: a comes after b
    int compare(T a, T b);
}