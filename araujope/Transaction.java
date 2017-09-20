/*
    Name: Augusto Araujo Peres Goncalez
    File:  Transaction.java
    Other Files in this Project:
    Main.java
    FXMLFinalProjectController.java
    FXMLFinalProject.fxml
    TransactionList.java
    Category.java
    main.css
    ids.txt
    transactions.txt
    
    Main class: Main.java
 */
package araujope;

import java.time.LocalDate;

/**
 * Class that models a Transaction object, which contains id, date, category,
 * method, value and observation fields. It verifies if each parameter given
 * for setting each data member is valid. If it is not, exceptions are thrown
 * with the proper message.
 *
 * @author Augusto Araujo Peres Goncalez
 */
public class Transaction implements Comparable<Transaction> {

    private int id;
    private LocalDate date;
    private Category category;
    private String method;
    private double value;
    private String observation = "NA";

    /**
     * Default constructor for an empty Transaction object.
     */
    public Transaction() {
    }
    
    /**
     * Constructs an object with given values for id, date, category, method,
     * value and observation. The id value has to be greater than or equal to
     * zero. The date given cannot be null. The category is an enum.
     * The method can not be an empty or null String. The value has to be 
     * greater than zero. The observation is optional, therefore if its 
     * parameter is null, the default value "NA" is kept. If any of the required
     * values are not met, an Exception is thrown.
     * 
     * @param id the programmer-specified id
     * @param date the programmer-specified date
     * @param category the programmer-specified category
     * @param method the programmer-specified method
     * @param value the programmer-specified value
     * @param observation the programmer-specified observation
     */
    public Transaction(int id, LocalDate date, Category category, String method,
        double value, String observation) {
        setId(id);
        setDate(date);
        setCategory(category);
        setMethod(method);
        setValue(value);
        setObservation(observation);
    }
    
    /**
     * Attempts to place a valid programmer-specified id into this Transaction's
     * id member. Id must be greater than or equal to zero, otherwise an 
     * Exception is thrown.
     * 
     * @param id the programmer-specified id
     */
    public void setId(int id) {
        
        // checks if id number is valid
        if (id >= 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("Invalid: id must be greater "
                    + "than or equal to zero.");
        }
    }
    
    /**
     * Retrieves this Transaction's id number
     * 
     * @return the unique id of this Transaction
     */
    public int getId() {
        return id;
    }
    
    /**
     * Attempts to place a valid programmer-specified date into this 
     * Transaction's date member. Date must not be empty, otherwise an Exception 
     * is thrown.
     * 
     * @param date the programmer-specified date
     */
    public void setDate(LocalDate date) {
        
        // checks to see if the date is not null
        if (date == null) {
            throw new IllegalArgumentException("Invalid: Date cannot be "
                    + "empty.");
        } else {
            this.date = date;
        }
    }
    
    /**
     * Retrieves this Transaction's date
     * 
     * @return the date of this Transaction
     */
    public LocalDate getDate() {
        return date;
    }
    
    /**
     * Place a valid programmer-specified category into this Transaction's 
     * category member. Since the value is an Enum, no validation is necessary.
     * 
     * @param cat the programmer-specified category
     */
    public void setCategory(Category cat) {
        category = cat;
    }
    
    /**
     * Retrieves this Transaction's category
     * 
     * @return the category of this Transaction
     */
    public Category getCategory() {
        return category;
    }
    
    /**
     * Attempts to place a valid programmer-specified method into this 
     * Transaction's method member. Method must not be null and it cannot be an
     * empty String, otherwise an Exception is thrown.
     * 
     * @param method the programmer-specified method
     */
    public void setMethod(String method) {
        if(method != null && !method.trim().equals("")){
            this.method = method;
        } else {
            throw new IllegalArgumentException("Invalid: method cannot be "
                    + "empty.");
        }
    }
    
    /**
     * Retrieves this Transaction's method.
     * 
     * @return the method of this Transaction
     */
    public String getMethod() {
        return method;
    }
    
    /**
     * Attempts to place a valid programmer-specified value into this 
     * Transaction's value member. Value must be greater than zero, otherwise an
     * Exception is thrown.
     * 
     * @param value the programmer-specified value
     */
    public void setValue(double value) {
        
        // checks if the value parameter is valid
        if(value > 0) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Invalid: value must be greater"
                    + " than zero.");
        }
    }
    
    /**
     * Retrieves this Transaction's value.
     * 
     * @return the value of this Transaction
     */
    public double getValue() {
        return value;
    }
    
    /**
     * If the observation parameter is either null or an empty String, the
     * default value "NA" (Non-applicable) is kept for the observation data 
     * member. Otherwise, places the value of the parameter into the data 
     * member.
     * 
     * @param observation the programmer-specified value
     */
    public void setObservation(String observation) {
        if (observation != null && !observation.trim().equals("")) {
            this.observation = observation;
        }
    }
    
    /**
     * Retrieves this Transaction's observation
     * 
     * @return the observation of this Transaction
     */
    public String getObservation() {
        return observation;
    }
    
    /**
     * Method that retrieves a String representation of this Transaction, 
     * containing all of its data members (id, date, category, method, value and
     * observation)
     * 
     * @return this Transaction's String representation 
     */
    @Override
    public String toString() {
        
        // transform the date into a String
        String strDate = String.format("%d/%d/%d", date.getMonthValue(), 
                date.getDayOfMonth(), date.getYear());
        
        // format observation and category to not take too much space
        String formatedObs = observation;
        String formatedCat = category.getName();
        
        if (observation.length() > 9) { // observation
            formatedObs = observation.substring(0, 10) + "...";
        }
        
        if (formatedCat.length() > 12) { // category
            formatedCat = formatedCat.substring(0, 9) + "...";
        }
        
        // TODO: Format properly
        return String.format("%05d  %-10s  %-12s  %-6s  %-7.2f  %s", id, strDate, 
                formatedCat, method, value, formatedObs);
    }

    /**
     * Method that compares this Transaction to another compared by its value.
     * If this object's value is greater than the parameter's value, than it 
     * returns 1, if the opposite occurs it return -1. If both values are the 
     * same, it returns 0.
     * 
     * @param t the Transaction being compared to this
     * @return 1 if this value is greater, -1 if it is less than or 0 if equal
     */
    @Override
    public int compareTo(Transaction t) {
        
        // compare which value is greater than the other
        if (value > t.getValue()) {
            return 1;
        } else if (value < t.getValue()) {
            return -1;
        } else {
            return 0;
        }
    }
}
