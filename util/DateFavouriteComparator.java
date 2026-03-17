package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.models.Favourite;

import java.util.Comparator;

public class DateFavouriteComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        IDFavouriteComparator comp = new IDFavouriteComparator();
        Favourite f1 = (Favourite) o1;
        Favourite f2 = (Favourite) o2;

        if (f1.getDateFavourited().compareTo(f2.getDateFavourited()) == 0) {
            return comp.compare(f1, f2);
        } else {
            return (-1)*(f1.getDateFavourited().compareTo(f2.getDateFavourited()));
        }

    }
}