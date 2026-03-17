package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.models.Restaurant;

import java.util.Comparator;

public class DateRestaurantComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        NameRestaurantComparator comp = new NameRestaurantComparator();
        Restaurant r1 = (Restaurant) o1;
        Restaurant r2 = (Restaurant) o2;

        if (r1.getDateEstablished().compareTo(r2.getDateEstablished()) == 0) {
            return comp.compare(r1, r2);
        } else {
            return r1.getDateEstablished().compareTo(r2.getDateEstablished());
        }

    }
}
