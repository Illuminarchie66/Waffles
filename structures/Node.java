package uk.ac.warwick.cs126.structures;

public class Node<E> {
    public E key;
    public Node left, right;

    public Node(E data) {
        key = data;
        left = right = null;
    }
}
