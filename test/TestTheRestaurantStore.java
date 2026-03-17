package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.models.*;
import uk.ac.warwick.cs126.stores.RestaurantStore;

public class TestTheRestaurantStore extends TestRunner {
    TestTheRestaurantStore() {
        System.out.println("\n[Testing RestaurantStore]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testAddRestaurant();
        testAddRestaurants();
        testGetRestaurant();
        testGetRestaurants();
        testGetRestaurantsByName();
        testGetRestaurantsByDateEstablished();
        testGetRestaurantsByDateEstablishedInputArray();
        testGetRestaurantsByWarwickStars();
        testGetRestaurantsByRating();
        testGetRestaurantsByDistanceFrom();
        testGetRestaurantsByDistanceFromInputArray();
        testGetRestaurantsContaining();
    }

    private void testAddRestaurant() {
        try {
            // Initialise new store
            // We do this for every method so we have a fresh copy without any data in it
            RestaurantStore restaurantStore = new RestaurantStore();

            // Create a restaurant object
            // Restaurant(String repeatedID,
            //            String firstNameOfOwner,
            //            String lastNameOfOwner,
            //            Cuisine cuisine,
            //            EstablishmentType establishment,
            //            PriceRange price,
            //            Date dateEstablished,
            //            float latitude,
            //            float longitude,
            //            boolean vegetarian,
            //            boolean vegan,
            //            boolean glutenFree,
            //            boolean nutFree,
            //            boolean lactoseFree,
            //            boolean halal,
            //            Date inspectionData,
            //            int inspectionRating,
            //            int warwickStars,
            //            int customerRating)
            Restaurant restaurant = new Restaurant(
                    "111222333444555611122233344455561112223334445556",
                    "TARDIS",
                    "Doctor",
                    "Who",
                    Cuisine.British,
                    EstablishmentType.Takeaway,
                    PriceRange.FineDining,
                    parseDate("1963-11-23 12:34:56"),
                    52.3838f,
                    -1.560065f,
                    true,
                    true,
                    true,
                    true,
                    true,
                    true,
                    parseDate("2005-03-26 12:34:56"),
                    5,
                    2,
                    4.0f
            );

            // Add to store
            boolean result = restaurantStore.addRestaurant(restaurant);

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testAddRestaurant()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testAddRestaurant()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testAddRestaurant()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testAddRestaurants() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed, should return true as all the data is valid
            boolean result = restaurantStore.addRestaurant(restaurants);

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testAddRestaurants()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testAddRestaurants()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testAddRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurant() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Should return true as the restaurant with ID 9299783561728336 exists
            boolean result = restaurantStore.getRestaurant(9299783561728336L) != null
                    && restaurantStore.getRestaurant(9299783561728336L).getID().equals(9299783561728336L);

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurant()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurant()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurant()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurants() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurants();

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-id.csv"));

            // Note: You have to set ID here as these do not get processed in the current RestaurantStore object
            // The gotRestaurants have proper IDs already as these have been processed in restaurantStore
            // The loadRestaurantDataToArray is a method that only gives us a Restaurant array from text data
            // It is not loaded into the restaurantStore object
            // We do not load anything into restaurantStore unless addRestaurant(r) is called
            // So, since we manually read the data, we can set IDs to their actual IDs
            expectedRestaurants[0].setID(1692228679459533L);
            expectedRestaurants[1].setID(1853896836596432L);
            expectedRestaurants[2].setID(1983542699134676L);
            expectedRestaurants[3].setID(3417685886353779L);
            expectedRestaurants[4].setID(4126217165326377L);
            expectedRestaurants[5].setID(4158933795222611L);
            expectedRestaurants[6].setID(5652995694831743L);
            expectedRestaurants[7].setID(9299783561728336L);
            expectedRestaurants[8].setID(9685731497851316L);
            expectedRestaurants[9].setID(9812977962372633L);

            // Now to compare and verify
            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getID().equals(expectedRestaurants[i].getID());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {

                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurants()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurants()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByName() {
        try {
            // Initialise new store
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByName();

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-name.csv"));

            expectedRestaurants[0].setID(4126217165326377L);
            expectedRestaurants[4].setID(1692228679459533L);
            expectedRestaurants[1].setID(1853896836596432L);
            expectedRestaurants[5].setID(1983542699134676L);
            expectedRestaurants[6].setID(3417685886353779L);
            expectedRestaurants[9].setID(4158933795222611L);
            expectedRestaurants[3].setID(5652995694831743L);
            expectedRestaurants[7].setID(9299783561728336L);
            expectedRestaurants[2].setID(9685731497851316L);
            expectedRestaurants[8].setID(9812977962372633L);

            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getName().equals(expectedRestaurants[i].getName());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByName()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByName()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByName()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDateEstablished() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByDateEstablished();

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-date.csv"));

            expectedRestaurants[0].setID(4126217165326377L);
            expectedRestaurants[4].setID(1692228679459533L);
            expectedRestaurants[1].setID(1853896836596432L);
            expectedRestaurants[5].setID(1983542699134676L);
            expectedRestaurants[6].setID(3417685886353779L);
            expectedRestaurants[9].setID(4158933795222611L);
            expectedRestaurants[3].setID(5652995694831743L);
            expectedRestaurants[7].setID(9299783561728336L);
            expectedRestaurants[2].setID(9685731497851316L);
            expectedRestaurants[8].setID(9812977962372633L);

            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getDateEstablished().equals(expectedRestaurants[i].getDateEstablished());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDateEstablished()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablished()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablished()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDateEstablishedInputArray() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByDateEstablished(restaurants);

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-date.csv"));

            expectedRestaurants[0].setID(4126217165326377L);
            expectedRestaurants[4].setID(1692228679459533L);
            expectedRestaurants[1].setID(1853896836596432L);
            expectedRestaurants[5].setID(1983542699134676L);
            expectedRestaurants[6].setID(3417685886353779L);
            expectedRestaurants[9].setID(4158933795222611L);
            expectedRestaurants[3].setID(5652995694831743L);
            expectedRestaurants[7].setID(9299783561728336L);
            expectedRestaurants[2].setID(9685731497851316L);
            expectedRestaurants[8].setID(9812977962372633L);

            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getDateEstablished().equals(expectedRestaurants[i].getDateEstablished());
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDateEstablishedInputArray()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByWarwickStars() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            // Add to store to be processed
            restaurantStore.addRestaurant(restaurants);

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByWarwickStars();

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-stars.csv"));

            expectedRestaurants[1].setID(1983542699134676L);
            expectedRestaurants[3].setID(4158933795222611L);
            expectedRestaurants[0].setID(5652995694831743L);
            expectedRestaurants[2].setID(9299783561728336L);
            expectedRestaurants[4].setID(9812977962372633L);

            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getWarwickStars() == expectedRestaurants[i].getWarwickStars();
                    if(!result){
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByWarwickStars()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByWarwickStars()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByWarwickStars()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByRating() {
        try {
            //TODO
            // Note: The loader does not load restaurants with ratings, default is 0.0 for all
            // You have to set them yourselves after loading it, use:
            //     restaurants[0].setCustomerRating(4.9f);
            // Alternatively, you can create your own restaurant objects with the constructor
            RestaurantStore restaurantStore = new RestaurantStore();

            // Load test data from /data folder
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));

            restaurants[0].setCustomerRating(3.2f);
            restaurants[1].setCustomerRating(1.6f);
            restaurants[2].setCustomerRating(2.1f);
            restaurants[3].setCustomerRating(3.7f);
            restaurants[4].setCustomerRating(4.9f);
            restaurants[5].setCustomerRating(1.1f);
            restaurants[6].setCustomerRating(0.0f);
            restaurants[7].setCustomerRating(1.9f);
            restaurants[8].setCustomerRating(4.8f);
            restaurants[9].setCustomerRating(4.3f);

            // Get sorted data by ID from store
            Restaurant[] gotRestaurants = restaurantStore.getRestaurantsByRating(restaurants);

            // Load separate data that was manually sorted to verify with
            Restaurant[] expectedRestaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10-sorted-by-rating.csv"));

            expectedRestaurants[4].setCustomerRating(0.0f);
            expectedRestaurants[7].setCustomerRating(1.1f);
            expectedRestaurants[5].setCustomerRating(1.6f);
            expectedRestaurants[3].setCustomerRating(1.9f);
            expectedRestaurants[0].setCustomerRating(2.1f);
            expectedRestaurants[8].setCustomerRating(3.2f);
            expectedRestaurants[9].setCustomerRating(3.7f);
            expectedRestaurants[6].setCustomerRating(4.3f);
            expectedRestaurants[1].setCustomerRating(4.8f);
            expectedRestaurants[2].setCustomerRating(4.9f);

            expectedRestaurants[0].setID(4126217165326377L);
            expectedRestaurants[1].setID(1692228679459533L);
            expectedRestaurants[2].setID(1853896836596432L);
            expectedRestaurants[3].setID(1983542699134676L);
            expectedRestaurants[4].setID(3417685886353779L);
            expectedRestaurants[5].setID(4158933795222611L);
            expectedRestaurants[6].setID(5652995694831743L);
            expectedRestaurants[7].setID(9299783561728336L);
            expectedRestaurants[8].setID(9685731497851316L);
            expectedRestaurants[9].setID(9812977962372633L);

            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getCustomerRating() == expectedRestaurants[i].getCustomerRating();
                    if(!result){
                        System.out.println(gotRestaurants[i]);
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (Restaurant r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Restaurant r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByRating()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByRating()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByRating()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDistanceFrom() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();
            Restaurant[] restaurants = restaurantStore.loadRestaurantDataToArray(
                    loadData("/test-restaurant/restaurant-10.csv"));
            restaurantStore.addRestaurant(restaurants);

            RestaurantDistance[] expectedRestaurants = new RestaurantDistance[10];
            expectedRestaurants[0] = new RestaurantDistance(null, 5689.7f);
            expectedRestaurants[1] = new RestaurantDistance(null, 5720.5f);
            expectedRestaurants[2] = new RestaurantDistance(null, 5746.8f);
            expectedRestaurants[3] = new RestaurantDistance(null, 5755.5f);
            expectedRestaurants[4] = new RestaurantDistance(null, 5803.7f);
            expectedRestaurants[5] = new RestaurantDistance(null, 5818.6f);
            expectedRestaurants[6] = new RestaurantDistance(null, 5892.5f);
            expectedRestaurants[7] = new RestaurantDistance(null, 5934.4f);
            expectedRestaurants[8] = new RestaurantDistance(null, 6091.8f);
            expectedRestaurants[9] = new RestaurantDistance(null, 6097.6f);

            RestaurantDistance[] gotRestaurants = restaurantStore.getRestaurantsByDistanceFrom(0.0f, 0.0f);

            boolean result = true;
            if (gotRestaurants.length == expectedRestaurants.length) {
                for (int i = 0; i < expectedRestaurants.length; i++) {
                    result = gotRestaurants[i].getDistance() == expectedRestaurants[i].getDistance();
                    if(!result){
                        System.out.println(gotRestaurants[i]);
                        break;
                    }
                }
            } else {
                result = false;
            }

            if(!result){
                System.out.println("\n[Expected]");
                for (RestaurantDistance r: expectedRestaurants){
                    System.out.println(r);
                }

                System.out.println("\n[Got]");
                if (gotRestaurants.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (RestaurantDistance r: gotRestaurants){
                    System.out.println(r);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
            } else {
                System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
                //System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFrom()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsByDistanceFromInputArray() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsByDistanceFromInputArray()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetRestaurantsContaining() {
        try {
            //TODO
            RestaurantStore restaurantStore = new RestaurantStore();

            boolean result = false;

            if (result) {
                System.out.println("[SUCCESS]    RestaurantStore: testGetRestaurantsContaining()");
            } else {
                //System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsContaining()");
                System.out.println("   [TODO]    RestaurantStore: testGetRestaurantsContaining()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    RestaurantStore: testGetRestaurantsContaining()");
            e.printStackTrace();
            System.out.println();
        }
    }
}
