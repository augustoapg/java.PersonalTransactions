/*
    Name: Augusto Araujo Peres Goncalez
    File:  TransactionList.java
    Other Files in this Project:
    Main.java
    FXMLFinalProjectController.java
    FXMLFinalProject.fxml
    Transaction.java
    Category.java
    main.css
    ids.txt
    transactions.txt
    
    Main class: Main.java
 */
package araujope;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Class that makes the object that contains an ArrayList of Transaction 
 * objects. It contains methods for going through each Transaction and searching
 * for the objects that matches the id, Category, method, date, date range or
 * value range, returning the indexes in which the Transaction match the search
 * made. It also verifies the parameters received for each method and throws
 * exceptions with proper messages for the invalid parameters.
 * 
 * @author Augusto Araujo Peres Goncalez
 */
public class TransactionList {

    // variable to control the array list capacity
    private int currentCapacity = 5;
    private ArrayList<Transaction> lstTransaction
            = new ArrayList(currentCapacity);

    /**
     * Default constructor for an empty TransactionList
     */
    public TransactionList() {

    }

    /**
     * Constructor that places any amount of Transaction objects into the
     * lstTransaction
     *
     * @param transaction the array of Transaction objects
     */
    public TransactionList(Transaction... transaction) {

        for (Transaction t : transaction) {

            // checks if capacity of the list was met
            if (lstTransaction.size() == currentCapacity) {

                // increases the capacity by 5
                currentCapacity += 5;
                lstTransaction.ensureCapacity(5);
            }

            // add Transaction object to the list
            lstTransaction.add(t);
        }
    }

    /**
     * Constructor that convert each record of a given file to Transaction
     * objects and place each of those in the lstTransaction. If any field in
     * the file is invalid, the exception is caught.
     *
     * @param file the file in which contains the Transaction objects
     * @throws FileNotFoundException if file does not exist
     */
    public TransactionList(File file) throws FileNotFoundException {

        // checks if file already exists
        if (file.exists()) {

            Scanner in = new Scanner(file);

            // while there are records
            while (in.hasNextLine()) {
                String record = in.nextLine();

                // get each field from the record
                String[] fields = record.split("\\|");

                // attempt to parse the id, date, categoty and value
                int id = Integer.parseInt(fields[0]);
                LocalDate date = LocalDate.parse(fields[1]);
                Category cat = Category.getCategory(fields[2]);
                double value = Double.parseDouble(fields[4]);

                // creates transaction object based on record's fields
                Transaction t = new Transaction(id, date, cat,
                        fields[3], value, fields[5]);

                // add the created Transaction to the list
                lstTransaction.add(t);
            }
        }
    }

    /**
     * Method that retrieves the amount of elements in the lstTransaction.
     *
     * @return the number of elements in the array list
     */
    public int size() {
        return lstTransaction.size();
    }

    /**
     * Method that retrieves the Transaction contained in the given index
     * position of the list.
     *
     * @param index the programmer-specified index
     * @return Transaction object in the index position of the lstTransaction
     */
    public Transaction get(int index) {

        // checks if index is valid
        if (index >= 0) {
            if (index < lstTransaction.size()) {
                return lstTransaction.get(index);
            } // if index is greater than the number of elements in the list
            else {
                throw new IllegalArgumentException("Invalid: Index must be less"
                        + " than the number of elements in the list.");
            }
        } else {  // if index less that 0
            throw new IllegalArgumentException("Invalid: Index must be greater"
                    + " than or equal to zero.");
        }
    }

    /**
     * Method that inserts a given Transaction object after the last occupied
     * index on the lstTransaction. If the capacity of the list is already full,
     * the capacity is increased by five before adding the new object.
     *
     * @param t the programmer-specified Transaction
     */
    public void add(Transaction t) {

        // checks if all indexes of the list are currently full
        if (lstTransaction.size() == currentCapacity) {
            currentCapacity += 5;
            lstTransaction.ensureCapacity(currentCapacity); // increase capacity
        }

        // adds new object to the first available index
        lstTransaction.add(t);
    }

    /**
     * Method that looks for a Transaction with a given id and returns its index
     * on the list. If the id is not found, a negative number is returned.
     *
     * @param id the programmer-specified id
     * @return the index of the Transaction or -1 if not found
     */
    public int searchId(int id) {

        // checks if id is valid
        if (id >= 0) {

            // check each Transaction in the list
            for (int i = 0; i < lstTransaction.size(); i++) {

                // return the index of the Transaction if the id matches
                if (lstTransaction.get(i).getId() == id) {
                    return i;
                }
            }
        } else { // invalid id
            throw new IllegalArgumentException("Invalid: Id must be greater"
                    + " than or equal to zero.");
        }

        // if id was not found
        return -1;
    }

    /**
     * Method that gets an Integer Array List and returns an int array with
     * equivalent values
     *
     * @param list the programmer-specified Integer list
     * @return an int array with the values of the ArrayList
     */
    private int[] convertToIntArray(ArrayList<Integer> list) {

        // declare array to be returned as the same size of the list
        int[] converted = new int[list.size()];

        // pass each list value to the new converted array
        for (int i = 0; i < list.size(); i++) {
            converted[i] = list.get(i);
        }

        return converted;
    }

    /**
     * Method that searches through all Transactions in the lstTransaction and
     * returns an int array with all the indexes in which the category String
     * being searched was matched with the Category's enum name of the object.
     *
     * @param category the programmer-specified category
     * @return an int array with the indexes where the category was found
     */
    public int[] searchCategory(String category) {

        // uses arrayList because don't know how many indexes will be found
        ArrayList<Integer> indexes;

        // checks validity of category
        if (category != null && !category.trim().equals("")) {

            // declare the array list of indexes
            indexes = new ArrayList(lstTransaction.size());

            // check each Transaction in the lsit
            for (int i = 0; i < lstTransaction.size(); i++) {
                // if a match is found
                if (lstTransaction.get(i).getCategory().
                        equals(Category.getCategory(category))) {
                    indexes.add(i); // add the index found to index list
                }
            }
        } else { // if category is null or empty String
            throw new IllegalArgumentException("Invalid: Category cannot be "
                    + "empty.");
        }

        // return the int array of indexes found
        return convertToIntArray(indexes);
    }

    /**
     * Method that searches through all Transactions in the lstTransaction and
     * returns an int array with all the indexes in which the date being
     * searched was found.
     *
     * @param date the programmer-specified date
     * @return an int array with the indexes where the date was found
     */
    public int[] searchDate(LocalDate date) {

        // uses arrayList because don't know how many indexes will be found
        ArrayList<Integer> indexes;

        // checks validity of date
        if (date != null) {

            // declare the array list of indexes
            indexes = new ArrayList(lstTransaction.size());

            // check each Transaction in the lsit
            for (int i = 0; i < lstTransaction.size(); i++) {
                // if a match is found
                if (lstTransaction.get(i).getDate().equals(date)) {
                    indexes.add(i); // add the index found to index list
                }
            }
        } else { // if date is null
            throw new IllegalArgumentException("Invalid: Date cannot be "
                    + "empty.");
        }

        // return the int array of indexes found
        return convertToIntArray(indexes);
    }

    /**
     * Method that searches through all Transactions in the lstTransaction and
     * returns an int array with all the indexes in which the method being
     * searched was found.
     *
     * @param method the programmer-specified method
     * @return an int array with the indexes where the method was found
     */
    public int[] searchMethod(String method) {

        // uses arrayList because don't know how many indexes will be found
        ArrayList<Integer> indexes;

        // checks validity of method
        if (method != null && !method.trim().equals("")) {

            // declare the array list of indexes
            indexes = new ArrayList(lstTransaction.size());

            // check each Transaction in the lsit
            for (int i = 0; i < lstTransaction.size(); i++) {
                // if a match is found
                if (lstTransaction.get(i).getMethod().equals(method)) {
                    indexes.add(i); // add the index found to index list
                }
            }
        } else { // if method is null or empty String
            throw new IllegalArgumentException("Invalid: Method cannot be "
                    + "empty.");
        }

        // return the int array of indexes found
        return convertToIntArray(indexes);
    }

    /**
     * Method that searches through all Transactions in the lstTransaction and
     * returns an int array with all the indexes in which the value is within
     * the range of values being searched.
     *
     * @param min the programmer-specified minimum value
     * @param max the programmer-specified maximum value
     * @return an int array with the indexes where the value criteria was found
     */
    public int[] searchValueRange(double min, double max) {

        // uses arrayList because don't know how many indexes will be found
        ArrayList<Integer> indexes;

        // checks validity of min and max values
        if (min > 0 && min <= max) {

            // declare the array list of indexes
            indexes = new ArrayList(lstTransaction.size());
            double value;

            // check each Transaction in the lsit
            for (int i = 0; i < lstTransaction.size(); i++) {

                value = lstTransaction.get(i).getValue();
                // if a match is found
                if (value >= min && value <= max) {
                    indexes.add(i); // add the index found to index list
                }
            }
        } else { // if values are invalid
            throw new IllegalArgumentException("Invalid: Values must be "
                    + "greater than zero, and maximum value has to be greater"
                    + " than the minimum value.");
        }

        // return the int array of indexes found
        return convertToIntArray(indexes);
    }

    /**
     * Method that searches through all Transactions in the lstTransaction and
     * returns an int array with all the indexes in which the date is within the
     * range of dates being searched.
     *
     * @param from the programmer-specified from date
     * @param to the programmer-specified to date
     * @return an int array with the indexes where the date criteria was found
     */
    public int[] searchDateRange(LocalDate from, LocalDate to) {

        // uses arrayList because don't know how many indexes will be found
        ArrayList<Integer> indexes;

        // checks if dates are null
        if (from != null && to != null) {

            // checks validity of dates
            if (to.isAfter(from) || to.isEqual(from)) {
                // declare the array list of indexes
                indexes = new ArrayList(lstTransaction.size());

                LocalDate date;

                // check each Transaction in the lsit
                for (int i = 0; i < lstTransaction.size(); i++) {

                    date = lstTransaction.get(i).getDate();

                    // if from <= date <= to
                    if ((date.isAfter(from) || date.isEqual(from))
                            && (date.isBefore(to) || date.isEqual(to))) {
                        indexes.add(i); // add the index found to index list
                    }
                }
            } else { // if date from is later than date to
                throw new IllegalArgumentException("Invalid: Date From has to "
                        + "be earlier than the Date To.");
            }
        } else { // if any date is null
            throw new IllegalArgumentException("Invalid: Dates cannot be "
                    + "empty.");
        }

        // return the int array of indexes found
        return convertToIntArray(indexes);
    }

    /**
     * Method that retrieves the index position on the lstTransaction for a
     * given Transaction object. If the object is not found a negative number is
     * returned instead. The Transaction object given has to be not null,
     * otherwise an exception is thrown.
     *
     * @param transaction the programmer-specified from transaction
     * @return the index of the transaction searched or -1 if not found
     */
    public int indexOf(Transaction transaction) {

        // check if object is valid
        if (transaction != null) {

            // search each transaction in the list for the searched one
            for (int i = 0; i < lstTransaction.size(); i++) {
                if (lstTransaction.get(i) == transaction) {
                    return i; // return index found
                }
            }

            // if index is never found
            return -1;

        } else {
            throw new IllegalArgumentException("Invalid: Transaction object "
                    + "cannot be null.");
        }
    }

    /**
     * Method that replaces a Transaction at a given index for a new given
     * Transaction. The index has to be greater than or equal to zero and has to
     * be of lesser value than the size of the lstTransaction.
     *
     * @param index the programmer-specified index
     * @param t the programmer-specified Transaction
     */
    public void set(int index, Transaction t) {

        // checks index's validity
        if (index >= 0 && index < lstTransaction.size()) {
            lstTransaction.set(index, t); // replace Transaction at index
        } else { // if index not valid
            throw new IllegalArgumentException("Invalid: Index must be positive"
                    + " and less than the size of the list.");
        }
    }

    /**
     * Method that deletes the Transaction from the lstTransaction at the index
     * position given. The index has to be positive and less than the size of
     * the list, otherwise an exception is thrown.
     *
     * @param index the programmer-specified index
     */
    public void remove(int index) {

        // checks if index is valid
        if (index >= 0 && index < lstTransaction.size()) {
            lstTransaction.remove(index);
        } else {
            throw new IllegalArgumentException("Invalid: Index must be greater"
                    + " than or equal to zero.");
        }
    }

    /**
     * Method that gets the lstSearch and order the results from the one with
     * the smallest value to the highest one
     */
    public void sortByValue() {
        Collections.sort(lstTransaction);
    }
}
