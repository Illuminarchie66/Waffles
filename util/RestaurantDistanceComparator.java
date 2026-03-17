package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.models.RestaurantDistance;
import uk.ac.warwick.cs126.util.NameRestaurantComparator;

import java.util.Comparator;

public class RestaurantDistanceComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        NameRestaurantComparator comp = new NameRestaurantComparator();
        RestaurantDistance r1 = (RestaurantDistance) o1;
        RestaurantDistance r2 = (RestaurantDistance) o2;

        if (r1.getDistance() == r2.getDistance()) {
            return comp.compare(r1.getRestaurant(), r2.getRestaurant());
        } else if (r1.getDistance() > r2.getDistance()) {
            return 1;
        } else {
            return -1;
        }
    }
}