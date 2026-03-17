package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Customer;

import java.util.Comparator;

public class IDCustomerComparator implements Comparator{
    public int compare(Object o1, Object o2) {
        Customer c1 = (Customer) o1;
        Customer c2 = (Customer) o2;

        if (c1.getID().equals(c2.getID())) {
            return 0;
        } else if (c1.getID() > c2.getID()) {
            return 1;
        } else {
            return -1;
        }
    }
}