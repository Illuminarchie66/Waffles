package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.interfaces.IDataChecker;

import uk.ac.warwick.cs126.models.Customer;
import uk.ac.warwick.cs126.models.Restaurant;
import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.models.Review;

import java.util.Date;
import java.util.stream.Stream;

public class DataChecker implements IDataChecker {

    public DataChecker() {
        // Initialise things here
    }

    public Long extractTrueID(String[] repeatedID) {
        if (repeatedID.length != 3) {
            return null;
        } else {
            if (repeatedID[0].equals(repeatedID[1]) || repeatedID[0].equals(repeatedID[2])) {
                return Long.parseLong(repeatedID[0]);
            } else if (repeatedID[1].equals(repeatedID[2])) {
                return Long.parseLong(repeatedID[1]);
            } else {
                return null; 
            }
        }
    }

    public boolean isValid(Long inputID) {
        int[] counter = new int[9];

        if (inputID == null) {
            return false;
        }

        if (String.valueOf(inputID).length() != 16) {
            return false;
        }

        long temp = inputID*10;
        for (int p=0; p < 16; p++) {
            temp = (long) (temp/10);
            int digit = (int) (temp%10);
            if (digit == 0){
                return false;
            } else {
                counter[digit-1]++;
                if (counter[digit-1] > 3){
                    return false;
                }
            }
        }

        if (((int) (temp/10))%10 != 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValid(Customer customer) {
        if (customer == null){
            return false;
        } else {
            if (customer.getID() == null
                    || customer.getFirstName() == null
                    || customer.getLastName() == null
                    || customer.getDateJoined() == null
                    || !isValid(customer.getID())) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isValid(Restaurant restaurant) {
        if (restaurant == null){
            return false;
        } else {
            if (restaurant.getRepeatedID() == null) {
                return false;
            } else {
                Long trueID = extractTrueID(restaurant.getRepeatedID());
                restaurant.setID(trueID);

                if (restaurant.getName() == null
                        || restaurant.getOwnerFirstName() == null
                        || restaurant.getOwnerLastName() == null
                        || restaurant.getCuisine() == null
                        || restaurant.getEstablishmentType() == null
                        || restaurant.getPriceRange() == null
                        || restaurant.getDateEstablished() == null
                        || restaurant.getLastInspectedDate() == null) {
                    if (restaurant.getName().contains("2 The")) {
                        System.out.print(restaurant.getName());
                        System.out.println(" null failure.");
                    }
                    return false;
                }

                if (!isValid(restaurant.getID())) {
                    if (restaurant.getName().contains("2 The")) {
                        System.out.print(restaurant.getName());
                        System.out.println(" invalid ID.");
                    }

                    return false;
                }

                if (restaurant.getFoodInspectionRating() > 5
                        || restaurant.getFoodInspectionRating() < 0
                        || restaurant.getWarwickStars() > 3
                        || restaurant.getWarwickStars() < 0) {
                    if (restaurant.getName().contains("2 The")) {
                        System.out.print(restaurant.getName());
                        System.out.println(" thing1.");
                    }
                    return false;
                }

                if (restaurant.getCustomerRating() > 5
                        || (restaurant.getCustomerRating() < 1.0f && restaurant.getCustomerRating() != 0.0f)
                        || (restaurant.getDateEstablished().after(restaurant.getLastInspectedDate()))) {
                    if (restaurant.getName().contains("2 The")) {
                        System.out.print(restaurant.getName());
                        System.out.println(" thing2.");
                    }
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public boolean isValid(Favourite favourite) {
        if (favourite == null){
            return false;
        } else {
            if (favourite.getID() == null
                    || favourite.getCustomerID() == null
                    || favourite.getRestaurantID() == null
                    || favourite.getDateFavourited() == null
                    || !isValid(favourite.getID())
                    || !isValid(favourite.getCustomerID())
                    || !isValid(favourite.getRestaurantID())) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isValid(Review review) {
        // TODO
        return false;
    }
}