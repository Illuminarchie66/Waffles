package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.util.NameRestaurantComparator;

import java.util.Comparator;

public class StarRestaurantComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        NameRestaurantComparator comp = new NameRestaurantComparator();
        Restaurant r1 = (Restaurant) o1;
        Restaurant r2 = (Restaurant) o2;

        if (r1.getWarwickStars() == (r2.getWarwickStars())) {
            return comp.compare(r1, r2);
        } else if (r1.getWarwickStars() > r2.getWarwickStars()){
            return 1;
        } else {
            return -1;
        }

    }
}