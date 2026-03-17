package uk.ac.warwick.cs126.structures;

public class MyHashSet<E> {

    private transient MyHashMap<E, Object> map;

    private static final Object PRESENT = new Object();

    public MyHashSet() {
        map = new MyHashMap<E, Object>();
    }

    public void add(E e) {
        map.put(e, PRESENT);
    }

    public void remove(E e) {
        map.delete(e);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(E e) {
        return map.containsKey(e);
    }
}


