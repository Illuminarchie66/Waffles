package uk.ac.warwick.cs126.stores;

import uk.ac.warwick.cs126.interfaces.ICustomerStore;
import uk.ac.warwick.cs126.models.*;
import uk.ac.warwick.cs126.structures.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import uk.ac.warwick.cs126.structures.MyHashMap;
import uk.ac.warwick.cs126.util.DataChecker;
import uk.ac.warwick.cs126.util.StringFormatter;
import uk.ac.warwick.cs126.util.IDCustomerComparator;
import uk.ac.warwick.cs126.util.NameCustomerComparator;

public class CustomerStore implements ICustomerStore {

    private MyHashMap<Long, Customer> customerHash;
    private MyBST<Customer> customerIDs;
    private MyBST<Customer> customerNames;

    private MyHashSet<Long> IDblacklist;
    private DataChecker dataChecker;
    private SortMethods sortMethods;
    private StringFormatter stringFormatter;

    public CustomerStore() {
        // Initialise variables here;

        customerHash = new MyHashMap<>();
        customerIDs = new MyBST<>();
        customerNames = new MyBST<>();

        IDblacklist = new MyHashSet<>();
        dataChecker = new DataChecker();
        sortMethods = new SortMethods();
        stringFormatter = new StringFormatter();
    }

    public Customer[] loadCustomerDataToArray(InputStream resource) {
        Customer[] customerArray = new Customer[0];

        try {
            byte[] inputStreamBytes = IOUtils.toByteArray(resource);
            BufferedReader lineReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int lineCount = 0;
            String line;
            while ((line=lineReader.readLine()) != null) {
                if (!("".equals(line))) {
                    lineCount++;
                }
            }
            lineReader.close();

            Customer[] loadedCustomers = new Customer[lineCount - 1];

            BufferedReader csvReader = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(inputStreamBytes), StandardCharsets.UTF_8));

            int customerCount = 0;
            String row;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (!("".equals(row))) {
                    String[] data = row.split(",");

                    Customer customer = (new Customer(
                            Long.parseLong(data[0]),
                            data[1],
                            data[2],
                            formatter.parse(data[3]),
                            Float.parseFloat(data[4]),
                            Float.parseFloat(data[5])));

                    loadedCustomers[customerCount++] = customer;
                }
            }
            csvReader.close();

            customerArray = loadedCustomers;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return customerArray;
    }

//    public boolean IDCheck(Long ID) {
//        for (int i = 0; i < customerArray.size(); i++) {
//            if (ID.equals(customerArray.get(i).getID())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean addCustomer(Customer customer) {
        if (!dataChecker.isValid(customer) || IDblacklist.contains(customer.getID())) {
            return false;
        } else {
//            if (IDCheck(customer.getID())) {
//                int i=0;
//                while (!customer.getID().equals(customerArray.get(i).getID())) {
//                    i++;
//                }
//                customerArray.removeByIndex(i);
//                IDblacklist.add(customer.getID());
//                return false;
//            } else {
//                customerArray.add(customer);
//                return true;
//            }
            if (customerHash.containsKey(customer.getID())) {
                customerNames.deleteKey(customerHash.get(customer.getID()), new NameCustomerComparator());
                customerIDs.deleteKey(customerHash.get(customer.getID()), new IDCustomerComparator());

                customerHash.delete(customer.getID());
                IDblacklist.add(customer.getID());

                return false;
            } else {
                customerNames.insert(customer, new NameCustomerComparator());
                customerIDs.insert(customer, new IDCustomerComparator());

                customerHash.put(customer.getID(), customer);

                return true;
            }
        }
    }

    public boolean addCustomer(Customer[] customers) {
        boolean successful = true;
        for (int i=0; i < customers.length; i++) {
            if (!addCustomer(customers[i])) {
                successful = false;
            } else {
            }
        }
        return successful;
    }

    public Customer getCustomer(Long id) {
        if (!customerHash.containsKey(id)) {
            return null;
        } else {
            return customerHash.get(id);
        }
    }

    public Customer[] getCustomers() {
//        Customer[] array = customerArray.toArray(Customer.class);
//        sortMethods.quickSort(array, 0, customerArray.size()-1, new IDCustomerComparator());
//        return array;
        return customerIDs.toArray(Customer.class);
    }

    public Customer[] getCustomers(Customer[] customers) {
//        Customer[] array = customers;
//        sortMethods.quickSort(array, 0, array.length-1, new IDCustomerComparator());
//        return array;
        MyBST<Customer> temp = new MyBST<>();
        for (int i=0; i < customers.length; i++) {
            temp.insert(customers[i], new IDCustomerComparator());
        }

        return temp.toArray(Customer.class);
    }

    public Customer[] getCustomersByName() {
//        Customer[] array = customerArray.toArray(Customer.class);
//        sortMethods.quickSort(array, 0, customerArray.size()-1, new NameCustomerComparator());
//        return array;
        return customerNames.toArray(Customer.class);
    }

    public Customer[] getCustomersByName(Customer[] customers) {
//        Customer[] array = customers;
//        sortMethods.quickSort(array, 0, array.length-1, new NameCustomerComparator());
//        return array;
        MyBST<Customer> temp = new MyBST<>();
        for (int i=0; i < customers.length; i++) {
            temp.insert(customers[i], new NameCustomerComparator());
        }
        return temp.toArray(Customer.class);
    }

    public Customer[] getCustomersContaining(String searchTerm) {
        if (searchTerm.isEmpty()) {
            return new Customer[0];
        }

//        MyArrayList<Customer> list = new MyArrayList<>();
//        NameCustomerComparator compareName = new NameCustomerComparator();
//        String searchTermConverted = stringFormatter.convertAccents(searchTerm);
//        String finalStr = searchTermConverted.toLowerCase();
//        for (int i=0; i< customerArray.size(); i++) {
//            String compareString = stringFormatter.convertAccents(customerArray.get(i).getFirstName()).toLowerCase() + " " + stringFormatter.convertAccents(customerArray.get(i).getLastName()).toLowerCase();
//            if (compareString.contains(finalStr)) {
//                int j=0;
//                while(true) {
//                    if (j == list.size()) {
//                        list.add(customerArray.get(i));
//                        break;
//                    } else if (compareName.compare(customerArray.get(i), list.get(j)) < 0) {
//                        list.add(customerArray.get(i), j);
//                        break;
//                    } else {
//                        j++;
//                    }
//                }
//            }
//        }
//        Customer[] arr = list.toArray(Customer.class);
        //MAKE SIMPLER
        // String searchTermConvertedFaster = stringFormatter.convertAccentsFaster(searchTerm);
        MyBST<Customer> temp = new MyBST<>();
        String search = stringFormatter.convertAccents(searchTerm).toLowerCase();
        containing_Recursive(search, temp, customerNames.root);

        return temp.toArray(Customer.class);
    }

    public void containing_Recursive(String searchTerm, MyBST<Customer> temp, Node<Customer> root) {
        if (root != null) {
            containing_Recursive(searchTerm, temp, root.left);
            String compareString = stringFormatter.convertAccents(root.key.getFirstName()).toLowerCase() + " " + stringFormatter.convertAccents(root.key.getLastName()).toLowerCase();
            if (compareString.contains(searchTerm)) {
                temp.insert(root.key, new NameCustomerComparator());
            }
            containing_Recursive(searchTerm, temp, root.right);
        }
    }

}
