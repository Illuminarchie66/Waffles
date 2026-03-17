package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Favourite;

import java.util.Comparator;

public class IDFavouriteComparator implements Comparator{
    public int compare(Object o1, Object o2) {
        Favourite f1 = (Favourite) o1;
        Favourite f2 = (Favourite) o2;

        if (f1.getID().equals(f2.getID())) {
            return 0;
        } else if (f1.getID() > f2.getID()) {
            return 1;
        } else {
            return -1;
        }
    }
}