package uk.ac.warwick.cs126.structures;
import uk.ac.warwick.cs126.models.Customer;

import java.lang.reflect.Array;
import java.util.Comparator;

public class SortMethods {

    public SortMethods() {
        //Initalise thin... wait what do I have to initalise??
    }

    public static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static <T> int partition(T[] arr, int low, int high, Comparator<? super T> comp) {
        T pivot = arr[high];
        int i = low -1;
        for (int j = low; j <= high-1; j++) {
            if (comp.compare(arr[j], pivot) < 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i+1, high);
        return (i+1);
    }

    public static <T> void quickSort(T[] arr, int low, int high, Comparator<? super T> comp) {
        if (low < high) {
            int temp = partition(arr, low, high, comp);
            quickSort(arr, low, temp-1, comp);
            quickSort(arr, temp+1, high, comp);
        }
    }

    public static <T> void mergeSort(T[] arr, int n, Comparator<? super T> comp, Class<T> clazz) {
        if (n < 2) {
            return;
        }
        int mid = n/2;
        T[] left = (T[]) Array.newInstance(clazz, mid);
        T[] right = (T[]) Array.newInstance(clazz, n-mid);

        for (int i = mid; i < mid; i++) {
            left[i] = arr[i];
        }

        for (int i = mid; i < n; i++) {
            right[i - mid] = arr[i];
        }

        mergeSort(left, mid, comp, clazz);
        mergeSort(right, n-mid, comp, clazz);

    }

    public static <T> void merge(T[] arr, T[] left, T[] right, int l, int r, Comparator<? super T> comp, Class<T> clazz) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < l && j < r) {
            if (comp.compare(left[i], right[i]) <= 0) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        while (i<l) {
            arr[k++] = left[i++];
        }

        while (j < r) {
            arr[k++] = right[j++];
        }
    }
}
