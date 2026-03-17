package uk.ac.warwick.cs126.structures;

import java.lang.reflect.Array;
import java.util.Comparator;

public class MyBST<E> {
    public Node root;

    public MyBST(){
        root = null;
    }

    public void deleteKey(E key, Comparator<? super E> comp) {
        root = delete_Recursive(root, key, comp);
    }

    Node delete_Recursive(Node root, E key, Comparator<? super E> comp) {
        if (root == null) {
            return root;
        }

        if (comp.compare(key, (E)root.key) < 0) {
            root.left = delete_Recursive(root.left, key, comp);
        } else if (comp.compare(key, (E)root.key) > 0) {
            root.right = delete_Recursive(root.right, key, comp);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.key = minValue(root.right);
            root.right = delete_Recursive(root.right, (E)root.key, comp);
        }
        return root;
    }

    public E min() {
        return minValue(root);
    }

    E minValue (Node root) {
        E minval = (E)root.key;
        while (root.left != null) {
            minval = (E)root.left.key;
            root = root.left;
        }
        return minval;
    }

    public void insert(E key, Comparator<? super E> comp) {
        root = insert_Recursive(root, key, comp);
    }

    Node insert_Recursive(Node root, E key, Comparator<? super E> comp) {
        if (root == null) {
            root = new Node(key);
            return root;
        }

        if (comp.compare(key, (E)root.key) < 0) {
            root.left = insert_Recursive(root.left, key, comp);
        } else if (comp.compare(key, (E)root.key) > 0) {
            root.right = insert_Recursive(root.right, key, comp);
        } else {
        }
        return root;
    }

    public void inorder() {
        inorder_Recursive(root);
    }

    void inorder_Recursive(Node root) {
        if (root != null) {
            inorder_Recursive(root.left);
            System.out.println(root.key + " ");
            inorder_Recursive(root.right);
        }
    }

    public boolean search(E key, Comparator<? super E> comp) {
        root = search_Recursive(root, key, comp);
        if (root != null) {
            return true;
        } else {
            return false;
        }
    }

    Node search_Recursive(Node root, E key, Comparator<? super E> comp) {
        if (root == null || comp.compare(key, (E) root.key) == 0) {
            return root;
        }

        if (comp.compare(key, (E)root.key) > 0) {
            return search_Recursive(root.left, key, comp);
        }

        return search_Recursive(root.right, key, comp);
    }

    public int size() {
        return(size(root));
    }
    private int size(Node node) {
        if (node == null) return(0);
        else {
            return(size(node.left) + 1 + size(node.right));
        }
    }

    public E[] toArray(Class<?> classTemp) {
        E[] temp = (E[]) Array.newInstance(classTemp, size());
        toArray_Recursive(root, temp, 0);
        return temp;
    }

    int toArray_Recursive(Node root, E[] array, int i) {
        if (root != null){
            i = toArray_Recursive(root.left, array, i);
            array[i++] = (E)root.key;
            i = toArray_Recursive(root.right, array, i);
        }
        return i;
    }


}
