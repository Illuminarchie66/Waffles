package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.util.IDRestaurantComparator;

import java.util.Comparator;

public class NameRestaurantComparator implements Comparator{
    public int compare(Object o1, Object o2) {
        IDRestaurantComparator comp = new IDRestaurantComparator();
        Restaurant r1 = (Restaurant) o1;
        Restaurant r2 = (Restaurant) o2;

        if (r1.getName().toLowerCase().compareTo(r2.getName().toLowerCase()) == 0) {
            return comp.compare(r1, r2);
        } else {
            return r1.getName().compareTo(r2.getName());
        }
    }
}