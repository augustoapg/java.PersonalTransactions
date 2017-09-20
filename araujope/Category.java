/*
    Name: Augusto Araujo Peres Goncalez
    File:  Category.java
    Other Files in this Project:
    Main.java
    FXMLFinalProjectController.java
    FXMLFinalProject.fxml
    TransactionList.java
    Transaction.java
    main.css
    ids.txt
    transactions.txt
    
    Main class: Main.java
*/

package araujope;

import java.util.HashMap;

/**
 * This class is an enum used for the list of possible Categories for the 
 * transactions.
 * 
 * @author Augusto Araujo Peres Goncalez
 */
public enum Category {
    RENT("Rent"),
    UTILITIES("Utilities"),
    GROCERIES("Groceries"),
    EATOUT("Eat Out"),
    GAS("Gas"),
    CAREXPENSES("Car Expenses"),
    PHARMACY("Pharmacy"),
    ENTERTAINMENT("Entertainment"),
    TRANSPORTATION("Transportation"),
    OTHER("Other");
    
    private String name;
    
    // constant used to iterate through all Category items
    public static final int SIZE = 10;
    
    // hash map used for reverse lookup
    private static HashMap<String, Category> lookupByName = null;
    
    /**
     * Constructs the enum and assign its respective name
     * 
     * @param name the programmer-specified name
     */
    private Category(String name) {
        this.name = name;
    }
    
    /**
     * Retrieves the String name of the Category enum
     * 
     * @return this enum String name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Method that gets the name of the category and returns the enum constant
     * that corresponds to it.
     * 
     * @param name String name of the constant
     * @return the enum constant correspondent to the name given
     */
    public static Category getCategory(String name) {
        
        // checks if HashMap was already initialized
        if (lookupByName == null) {
            // populate HashMap for the first time
            initNameLookup();
        }
        
        // get the Category constant from HashMap
        Category c = lookupByName.get(name);
        
        // in case the name was not found throws exception
        if (c == null) {
            throw new IllegalArgumentException("Invalid Category.");
        }
        
        return c;
    }
    
    /**
     * Static class used for populating a HashMap with the name of the category
     * as the key and with its respective constant, in order to make it possible
     * to do a reverse lookup using the name of the Category.
     */
    private static void initNameLookup() {
        // creates the HashMap object that get the names and Category
        lookupByName = new HashMap<String, Category>();
        
        // populates the HashMap
        for(Category c : values()) {
            lookupByName.put(c.name, c);
        }
    }
}
