package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IRestaurantStore;
import uk.ac.warwick.cs126.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.*;

import uk.ac.warwick.cs126.structures.MyHashMap;
import uk.ac.warwick.cs126.util.*;

public class RestaurantStore implements IRestaurantStore {

    private MyArrayList<Restaurant> restaurantArray;

    private MyHashMap<Long, Restaurant> restaurantHash;
    private MyBST<Restaurant> restaurantIDs;
    private MyBST<Restaurant> restaurantNames;
    private MyBST<Restaurant> restaurantDates;
    private MyBST<Restaurant> restaurantStars;
    private MyBST<Restaurant> restaurantRatings;

    private DataChecker dataChecker;
    private MyHashSet<Long> IDblacklist;
    private SortMethods sortMethods;
    private StringFormatter stringFormatter;
    private ConvertToPlace convertToPlace;

    public RestaurantStore() {
        // Initialise variables here
        restaurantArray = new MyArrayList<>();

        restaurantHash = new MyHashMap<>();
        restaurantIDs = new MyBST<>();
        restaurantNames = new MyBST<>();
        restaurantDates = new MyBST<>();
        restaurantStars = new MyBST<>();
        restaurantRatings = new MyBST<>();

        IDblacklist = new MyHashSet<>();
        dataChecker = new DataChecker();
        sortMethods = new SortMethods();
        stringFormatter = new StringFormatter();
        convertToPlace = new ConvertToPlace();
    }

    public Restaurant[] loadRestaurantDataToArray(InputStream resource) {
        Restaurant[] restaurantArray = new Restaurant[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Restaurant[] loadedRestaurants = new Restaurant[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            String row;
            int restaurantCount = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Restaurant restaurant = new Restaurant(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            Cuisine.valueOf(data[4]),
                            EstablishmentType.valueOf(data[5]),
                            PriceRange.valueOf(data[6]),
                            formatter.parse(data[7]),
                            Float.parseFloat(data[8]),
                            Float.parseFloat(data[9]),
                            Boolean.parseBoolean(data[10]),
                            Boolean.parseBoolean(data[11]),
                            Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]),
                            Boolean.parseBoolean(data[14]),
                            Boolean.parseBoolean(data[15]),
                            formatter.parse(data[16]),
                            Integer.parseInt(data[17]),
                            Integer.parseInt(data[18]));

                    loadedRestaurants[restaurantCount++] = restaurant;
                }
            }
            csvReader.close();

            restaurantArray = loadedRestaurants;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return restaurantArray;
    }

//    public boolean IDCheck(Long ID) {
//        for (int i = 0; i < restaurantArray.size(); i++) {
//            if (ID.equals(restaurantArray.get(i).getID())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean addRestaurant(Restaurant restaurant) {
        if (!dataChecker.isValid(restaurant)) {
            return false;
        }

        if (IDblacklist.contains(restaurant.getID())) {
            return false;
        }

        if (restaurantHash.containsKey(restaurant.getID())) {
            restaurantIDs.deleteKey(restaurantHash.get(restaurant.getID()), new IDRestaurantComparator());
            restaurantNames.deleteKey(restaurantHash.get(restaurant.getID()), new NameRestaurantComparator());
            restaurantDates.deleteKey(restaurantHash.get(restaurant.getID()), new DateRestaurantComparator());
            restaurantStars.deleteKey(restaurantHash.get(restaurant.getID()), new StarRestaurantComparator());
            restaurantRatings.deleteKey(restaurantHash.get(restaurant.getID()), new RatingRestaurantComparator());

            restaurantHash.delete(restaurant.getID());
            IDblacklist.add(restaurant.getID());

            return false;
        } else {
            restaurantIDs.insert(restaurant, new IDRestaurantComparator());
            restaurantNames.insert(restaurant, new NameRestaurantComparator());
            restaurantDates.insert(restaurant, new DateRestaurantComparator());
            restaurantRatings.insert(restaurant, new RatingRestaurantComparator());
            if (restaurant.getWarwickStars() != 0) {restaurantStars.insert(restaurant, new StarRestaurantComparator());}

            restaurantHash.put(restaurant.getID(), restaurant);
            return true;
        }
    }

    public boolean addRestaurant(Restaurant[] restaurants) {
        boolean successful = true;
        for (int i=0; i < restaurants.length; i++) {
            if (!addRestaurant(restaurants[i])) {
                successful = false;
            }
        }

        return successful;
    }

    public Restaurant getRestaurant(Long id) {
//        for (int i=0; i < restaurantArray.size(); i++){
//            if (id.equals(restaurantArray.get(i).getID())) {
//                return restaurantArray.get(i);
//            }
//        }
//        return null;
        if (!restaurantHash.containsKey(id)) {
            return null;
        } else {
            return restaurantHash.get(id);
        }
    }

    public Restaurant[] getRestaurants() {
//        Restaurant[] array = restaurantArray.toArray(Restaurant.class);
//        sortMethods.quickSort(array, 0, restaurantArray.size()-1, new IDRestaurantComparator());
//        return array;
        return restaurantIDs.toArray(Restaurant.class);
    }

    public Restaurant[] getRestaurants(Restaurant[] restaurants) {
//        Restaurant[] array = restaurants;
//        sortMethods.quickSort(array, 0, array.length-1, new IDRestaurantComparator());
//        return array;
        MyBST<Restaurant> temp = new MyBST<>();
        for (int i=0; i < restaurants.length; i++) {
            temp.insert(restaurants[i], new IDRestaurantComparator());
        }

        return temp.toArray(Restaurant.class);
    }

    public Restaurant[] getRestaurantsByName() {
//        Restaurant[] array = restaurantArray.toArray(Restaurant.class);
//        sortMethods.quickSort(array, 0, restaurantArray.size()-1, new NameRestaurantComparator());
//        return array;
        return restaurantNames.toArray(Restaurant.class);
    }

    public Restaurant[] getRestaurantsByDateEstablished() {
//        Restaurant[] array = restaurantArray.toArray(Restaurant.class);
//        sortMethods.quickSort(array, 0, restaurantArray.size()-1, new DateRestaurantComparator());
//        return array;
        return restaurantDates.toArray(Restaurant.class);
    }

    public Restaurant[] getRestaurantsByDateEstablished(Restaurant[] restaurants) {
//        Restaurant[] array = restaurants;
//        sortMethods.quickSort(array, 0, array.length-1, new DateRestaurantComparator());
//        return array;
        MyBST<Restaurant> temp = new MyBST<>();
        for (int i=0; i < restaurants.length; i++) {
            temp.insert(restaurants[i], new DateRestaurantComparator());
        }

        return temp.toArray(Restaurant.class);
    }

    public Restaurant[] getRestaurantsByWarwickStars() {
//        MyArrayList<Restaurant> tempList = new MyArrayList<>();
//        for (int i=0; i < restaurantArray.size(); i++) {
//            if (restaurantArray.get(i).getWarwickStars() != 0) {
//                tempList.add(restaurantArray.get(i));
//            }
//        }
//
//        Restaurant[] array = tempList.toArray(Restaurant.class);
//        sortMethods.quickSort(array, 0, array.length-1, new StarRestaurantComparator());
//        return array;
        return restaurantStars.toArray(Restaurant.class);
    }

    public Restaurant[] getRestaurantsByRating(Restaurant[] restaurants) {
//        Restaurant[] array = restaurants;
//        sortMethods.quickSort(array, 0, array.length-1, new RatingRestaurantComparator());
//        return array;
        return restaurantRatings.toArray(Restaurant.class);
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(float latitude, float longitude) {
//        RestaurantDistance[] disArray = new RestaurantDistance[restaurantArray.size()];
//        for (int i=0; i < restaurantArray.size(); i++) {
//            RestaurantDistance temp = new RestaurantDistance(restaurantArray.get(i), HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurantArray.get(i).getLatitude(), restaurantArray.get(i).getLongitude()));
//            disArray[i] = temp;
//        }
//
//        sortMethods.quickSort(disArray, 0, disArray.length-1, new RestaurantDistanceComparator());
//        return disArray;
        Restaurant[] array = restaurantIDs.toArray(Restaurant.class);
        MyBST<RestaurantDistance> disTree = new MyBST<>();
        for (int i=0; i < array.length; i++) {
            RestaurantDistance temp = new RestaurantDistance(array[i], HaversineDistanceCalculator.inKilometres(latitude, longitude, array[i].getLatitude(), array[i].getLongitude()));
            disTree.insert(temp, new RestaurantDistanceComparator());
        }
        return disTree.toArray(RestaurantDistance.class);
    }

    public RestaurantDistance[] getRestaurantsByDistanceFrom(Restaurant[] restaurants, float latitude, float longitude) {
//        RestaurantDistance[] disArray = new RestaurantDistance[restaurants.length];
//        for (int i=0; i < restaurants.length; i++) {
//            RestaurantDistance temp = new RestaurantDistance(restaurantArray.get(i), HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurants[i].getLatitude(), restaurants[i].getLongitude()));
//            disArray[i] = temp;
//        }
//
//        sortMethods.quickSort(disArray, 0, disArray.length-1, new RestaurantDistanceComparator());
//        return disArray;
        MyBST<RestaurantDistance> disTree = new MyBST<>();
        for (int i=0; i < restaurants.length; i++) {
            RestaurantDistance temp = new RestaurantDistance(restaurants[i], HaversineDistanceCalculator.inKilometres(latitude, longitude, restaurants[i].getLatitude(), restaurants[i].getLongitude()));
            disTree.insert(temp, new RestaurantDistanceComparator());
        }
        return disTree.toArray(RestaurantDistance.class);
    }

    public boolean cuisineMatch(String searchTerm, Restaurant restaurant) {
        String compareString = stringFormatter.normalise(restaurant.getCuisine().toString());
        if (compareString.contains(searchTerm)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean placeMatch(String searchTerm, Restaurant restaurant) {
        Place place = convertToPlace.convert(restaurant.getLatitude(), restaurant.getLongitude());
        String compareString = stringFormatter.normalise(place.getName());
        if (compareString.contains(searchTerm)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean nameMatch(String searchTerm, Restaurant restaurant) {
        String compareString = stringFormatter.normalise(restaurant.getName());
        if (compareString.contains(searchTerm)) {
            return true;
        } else {
            return false;
        }
    }

    public Restaurant[] getRestaurantsContaining(String searchTerm) {
        if (searchTerm.isEmpty()) {
            return new Restaurant[0];
        }
//        MyArrayList<Restaurant> matches = new MyArrayList<>();
//        String converted = stringFormatter.normalise(searchTerm);
//        for (int i=0; i < restaurantArray.size(); i++) {
//            if (cuisineMatch(converted, restaurantArray.get(i)) || nameMatch(converted, restaurantArray.get(i)) || placeMatch(converted, restaurantArray.get(i))) {
//                matches.add(restaurantArray.get(i));
//            }
//        }
//
//        Restaurant[] array = matches.toArray(Restaurant.class);
//        sortMethods.quickSort(array, 0, array.length-1, new NameRestaurantComparator());
//        for (int i=0; i < array.length; i++) {
//            System.out.println(i);
//            System.out.println(array[i]);
//        }
//        return array;

        MyBST<Restaurant> temp = new MyBST<>();
        String search = stringFormatter.convertAccentsFaster(searchTerm).toLowerCase();
        containing_Recursive(search, temp, restaurantNames.root, search);
        System.out.println(temp.toArray(Restaurant.class).length);

        Restaurant[] rests = temp.toArray(Restaurant.class);
        for (int i=0; i < temp.size(); i++) {
            System.out.println(rests[i]);
        }
        return rests;
//        return new Restaurant[0];
    }

    public void containing_Recursive(String searchTerm, MyBST<Restaurant> temp, Node<Restaurant> root, String converted) {
        if (root != null) {
            containing_Recursive(searchTerm, temp, root.left, converted);
            if (cuisineMatch(converted, root.key) || nameMatch(converted, root.key) || placeMatch(converted, root.key)) {
                temp.insert(root.key, new NameRestaurantComparator());
            }
            containing_Recursive(searchTerm, temp, root.right, converted);
        }
    }
}
