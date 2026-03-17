package uk.ac.warwick.cs126.structures;

public class MyStack<E> {
    private MyArrayList<E> lst;
    private int top;
    private int capacity;

    public MyStack() {
        lst = new MyArrayList<>();
        top = -1;
    }

    public void push(E elem) {
        top++;
        lst.add(elem);
    }

    public E pop() {
        if (lst.isEmpty()) {
            return null;
        } else {
            return lst.get(top--);
        }
    }

    public E peek() {
        if (lst.isEmpty()) {
            return null;
        } else {
            return lst.get(top);
        }
    }

    public int size() {
        return top+1;
    }

    public Boolean isEmpty() {
        return lst.isEmpty();
    }
}
