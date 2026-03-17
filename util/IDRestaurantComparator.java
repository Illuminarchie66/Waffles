package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Restaurant;

import java.util.Comparator;

public class IDRestaurantComparator implements Comparator{
    public int compare(Object o1, Object o2) {
        Restaurant c1 = (Restaurant) o1;
        Restaurant c2 = (Restaurant) o2;

        if (c1.getID().equals(c2.getID())) {
            return 0;
        } else if (c1.getID() > c2.getID()) {
            return 1;
        } else {
            return -1;
        }
    }
}