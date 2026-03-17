package uk.ac.warwick.cs126.util;
import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.util.IDCustomerComparator;

import java.util.Comparator;

public class NameCustomerComparator implements Comparator{
    public int compare(Object o1, Object o2) {
        IDCustomerComparator comp = new IDCustomerComparator();
        Customer c1 = (Customer) o1;
        Customer c2 = (Customer) o2;

        if (c1.getLastName().toLowerCase().compareTo(c2.getLastName().toLowerCase()) == 0) {
            if (c1.getFirstName().toLowerCase().compareTo(c2.getFirstName().toLowerCase()) == 0) {
                return comp.compare(c1, c2);
            } else if (c1.getFirstName().toLowerCase().compareTo(c2.getFirstName().toLowerCase()) > 0) {
                return 1;
            } else {
                return -1;
            }
        } else if (c1.getLastName().toLowerCase().compareTo(c2.getLastName().toLowerCase()) > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}