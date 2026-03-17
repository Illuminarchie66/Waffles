package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.IFavouriteStore;
import uk.ac.warwick.cs126.models.Favourite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.MyHashMap;

import uk.ac.warwick.cs126.structures.*;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.DateFavouriteComparator;
import uk.ac.warwick.cs126.util.IDFavouriteComparator;

public class FavouriteStore implements IFavouriteStore {

    private MyArrayList<Favourite> favouriteArray;
    private MyHashMap<Long, Favourite> favouriteHash;
    private MyBST<Favourite> favouriteIDs;

    private MyHashMap<Long, MyBST<Favourite>> favouriteCustomers;
    private MyHashMap<Long, MyBST<Favourite>> favouriteRestaurants;

    private MyHashMap<Long, MyHashMap<Long, Favourite>> customerToRestaurant;

    private MyHashMap<Long, MyBST<Favourite>> IDblacklist;
    private DataChecker dataChecker;

    public FavouriteStore() {
        // Initialise variables here
        favouriteArray = new MyArrayList<>();
        favouriteHash = new MyHashMap<>();
        favouriteIDs = new MyBST<>();
        favouriteCustomers = new MyHashMap<>();
        favouriteRestaurants = new MyHashMap<>();
        customerToRestaurant = new MyHashMap<>();
        dataChecker = new DataChecker();
        IDblacklist = new MyHashMap<>();
    }

    public Favourite[] loadFavouriteDataToArray(InputStream resource) {
        Favourite[] favouriteArray = new Favourite[0];

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

            Favourite[] loadedFavourites = new Favourite[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int favouriteCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");
                    Favourite favourite = new Favourite(
                            Long.parseLong(data[0]),
                            Long.parseLong(data[1]),
                            Long.parseLong(data[2]),
                            formatter.parse(data[3]));
                    loadedFavourites[favouriteCount++] = favourite;
                }
            }
            csvReader.close();

            favouriteArray = loadedFavourites;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return favouriteArray;
    }

    public boolean addFavourite(Favourite favourite) {
        if (!dataChecker.isValid(favourite)) {
            return false;
        }

        if (IDblacklist.containsKey(favourite.getID())) {
            return false;
        }

        Boolean contains = false;
        int container = 0;
        for (int i=0; i < favouriteArray.size(); i++) {
            if (favouriteArray.get(i).getCustomerID().equals(favourite.getCustomerID()) && favouriteArray.get(i).getRestaurantID().equals(favourite.getRestaurantID())) {
                contains = true;
                container = i;
                break;
            }
        }

        if (contains){
            if (favouriteArray.get(container).getDateFavourited().after(favourite.getDateFavourited())) {
                // add favourite to the blacklist store
                addToHashTree(favourite, favourite.getID(), IDblacklist, new DateFavouriteComparator());

                return false;
            } else {
                // replace container with favourite, add container to the blacklist store
                addToHashTree(favourite, favourite.getID(), IDblacklist, new DateFavouriteComparator());

                deleteFromStores(favouriteArray.get(container));
                addToStores(favourite);

                return true;

            }
        }

        if (favouriteHash.containsKey(favourite.getID())) {
            // Remove from stores, and black list the favorite
            // Then we remove
            deleteFromStores(favouriteHash.get(favourite.getID()));
            MyBST<Favourite> tree = new MyBST<>();
            tree.insert(favourite, new DateFavouriteComparator());
            IDblacklist.put(favourite.getID(), tree);

            return false;
        } else {
            addToStores(favourite);
            return true;
        }

    }

    public void addToStores(Favourite favourite) {
        favouriteArray.add(favourite);
        favouriteHash.put(favourite.getID(), favourite);
        favouriteIDs.insert(favourite, new IDFavouriteComparator());

        addToHashTree(favourite, favourite.getCustomerID(), favouriteCustomers, new DateFavouriteComparator());
        addToHashTree(favourite, favourite.getRestaurantID(), favouriteRestaurants, new DateFavouriteComparator());

        MyHashMap<Long, Favourite> temp = customerToRestaurant.get(favourite.getCustomerID());
        if (temp == null) {
            temp = new MyHashMap<>();
        }
        temp.put(favourite.getRestaurantID(), favourite);
        customerToRestaurant.delete(favourite.getCustomerID());
        customerToRestaurant.put(favourite.getCustomerID(), temp);
    }

    public void deleteFromStores(Favourite favourite) {
        favouriteArray.remove(favourite);
        favouriteHash.delete(favourite.getID());
        favouriteIDs.deleteKey(favourite, new IDFavouriteComparator());

        deleteFromHashTree(favourite, favourite.getCustomerID(), favouriteCustomers, new DateFavouriteComparator());
        deleteFromHashTree(favourite, favourite.getRestaurantID(), favouriteRestaurants, new DateFavouriteComparator());

        MyHashMap<Long, Favourite> temp = customerToRestaurant.get(favourite.getCustomerID());
        if (temp != null) {
            temp.delete(favourite.getRestaurantID());
            customerToRestaurant.delete(favourite.getCustomerID());
            customerToRestaurant.put(favourite.getCustomerID(), temp);
        }
    }

    public void addToHashTree(Favourite favourite, Long ID, MyHashMap<Long, MyBST<Favourite>> hashTree, Comparator<? super Favourite> comp) {
        MyBST<Favourite> tree = hashTree.get(ID);
        if (tree == null) {
            tree = new MyBST<>();
        }
        tree.insert(favourite, comp);
        hashTree.delete(ID);
        hashTree.put(ID, tree);
    }

    public void deleteFromHashTree(Favourite favourite, Long ID, MyHashMap<Long, MyBST<Favourite>> hashTree, Comparator<? super Favourite> comp) {
        MyBST<Favourite> tree = hashTree.get(ID);
        if (tree != null) {
            tree.deleteKey(favourite, comp);
            hashTree.delete(ID);
            hashTree.put(ID, tree);
        }
    }

    public boolean addFavourite(Favourite[] favourites) {
        boolean successful = true;
        for (int i=0; i < favourites.length; i++) {
            if (!addFavourite(favourites[i])) {
                successful = false;
            }
        }
        //System.out.println(favouriteArray.size());
        return successful;
    }

    public Favourite getFavourite(Long id) {
        if (!favouriteHash.containsKey(id)) {
            return null;
        } else {
            return favouriteHash.get(id);
        }
    }

    public Favourite[] getFavourites() {
        return favouriteIDs.toArray(Favourite.class);
    }

    public Favourite[] getFavouritesByCustomerID(Long id) {
        if (id == null || favouriteCustomers.get(id) == null) {
            return new Favourite[0];
        } else {
//            Favourite[] temp = favouriteCustomers.get(id).toArray(Favourite.class);
//            for (int i=0; i < temp.length; i++) {
//                System.out.println(temp[i]);
//            }
//            System.out.print(id);
//            System.out.print(": ");
//            favouriteCustomers.get(id).inorder();
            return favouriteCustomers.get(id).toArray(Favourite.class);
        }
    }

    public Favourite[] getFavouritesByRestaurantID(Long id) {
        if (id == null || favouriteRestaurants.get(id) == null) {
            return new Favourite[0];
        } else {
            return favouriteRestaurants.get(id).toArray(Favourite.class);
        }
    }

    public Long[] getCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODOgfgg
        if (favouriteCustomers.get(customer1ID) == null || favouriteCustomers.get(customer2ID) == null) {
            return new Long[0];
        }

//        MyBST<Favourite> tree = new MyBST<>();
//        MyArrayList<Long> list = new MyArrayList<>();
//
//        MyBST<Favourite> customers = favouriteCustomers.get(customer1ID);
//
//        MyHashSet<Long> IDSet;
//        IDSet = getHashSet(favouriteCustomers.get(customer2ID));
//
//        getCommon_Recursive(customers.root, IDSet, tree);
//        tree.inorder();
//        getIDList_Recursive(tree.root, list);
//
//        return list.toArray(Long.class);
        MyBST<Favourite> tree = favouriteCustomers.get(customer1ID);
        MyHashMap<Long, Favourite> restaurantIDs = getHashMap(favouriteCustomers.get(customer2ID));

//        if (restaurantIDs.containsKey(root.key.getRestaurantID())) {
//            root.get
//        }
        //tree.inorder();
        return new Long[0];
    }

    public MyHashMap<Long, Favourite> getHashMap(MyBST<Favourite> tree) {
        MyHashMap<Long, Favourite> hashMap = new MyHashMap<>();
        getHashMap_Recursive(tree.root, hashMap);
        return hashMap;
    }

    public void getHashMap_Recursive(Node<Favourite> root, MyHashMap<Long, Favourite> hashMap) {
        getHashMap_Recursive(root.left, hashMap);
        hashMap.put(root.key.getRestaurantID(), root.key);
        getHashMap_Recursive(root.right, hashMap);
    }

//    public void getCommon_Recursive(Node<Favourite> root, MyHashSet<Long> IDSet, MyBST<Favourite> tree) {
//        if (root != null) {
//            getCommon_Recursive(root.left, IDSet, tree);
//            if (IDSet.contains(root.key.getRestaurantID())) {
//                tree.insert(root.key, new DateFavouriteComparator());
//            }
//            getCommon_Recursive(root.right, IDSet, tree);
//        }
//    }
//
//    public void getIDList_Recursive(Node<Favourite> root, MyArrayList<Long> list) {
//        if (root != null) {
//            getIDList_Recursive(root.left, list);
//            list.add(root.key.getRestaurantID());
//            getIDList_Recursive(root.right, list);
//        }
//    }
//
//    public MyHashSet<Long> getHashSet(MyBST<Favourite> tree) {
//        MyHashSet<Long> hashSet = new MyHashSet<>();
//        getHashSet_Recursive(tree.root, hashSet);
//        return hashSet;
//    }
//
//    public void getHashSet_Recursive(Node<Favourite> root, MyHashSet<Long> hashSet) {
//        if (root != null) {
//            getHashSet_Recursive(root.left, hashSet);
//            hashSet.add(root.key.getRestaurantID());
//            getHashSet_Recursive(root.right, hashSet);
//        }
//    }

    public Long[] getMissingFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getNotCommonFavouriteRestaurants(Long customer1ID, Long customer2ID) {
        // TODO
        return new Long[0];
    }

    public Long[] getTopCustomersByFavouriteCount() {
        // TODO
        return new Long[20];
    }

    public Long[] getTopRestaurantsByFavouriteCount() {
        // TODO
        return new Long[20];
    }
}
