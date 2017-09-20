/*
    Name: Augusto Araujo Peres Goncalez
    File:  FXMLFinalProjectController.java
    Other Files in this Project:
    Main.java
    FXMLFinalProject.fxml
    Transaction.java
    TransactionList.java
    Category.java
    main.css
    ids.txt
    transactions.txt
    
    Main class: Main.java
 */
package araujope;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Controller class for the application. Responsible for all the functionality
 * of the program. It takes care of adding all of the entries from the fields
 * into the files, as well as showing messages indicating if each process was
 * a success or if generated any failures.
 * 
 * @author Augusto Araujo Peres Goncalez
 */
public class FXMLFinalProjectController implements Initializable {

    @FXML
    private Label lblOutput, lblOutputSearch, lblTotal;

    @FXML
    private TextField txtID, txtValue, txtIDS, txtValueFrom, txtValueTo;

    @FXML
    private DatePicker dpDate, dpDateS, dpDateFrom, dpDateTo;

    @FXML
    private ComboBox<String> cbxCategory, cbxCategoryS, cbxMethod;

    @FXML
    private TextArea txtObs;

    @FXML
    private RadioButton optDebit, optCredit, optCash, optID, optCategory,
            optDate, optMethod, optValue, optDateRange;

    @FXML
    private Button btnEditSearch, btnEdit, btnDelete, btnExit;

    @FXML
    private ListView<Transaction> lstSearch;

    @FXML
    private BorderPane bpMain, bpSearch;

    @FXML
    private GridPane grdMain;

    private TransactionList list = new TransactionList();
    private Transaction transaction = new Transaction();

    private ArrayList<Integer> ids = new ArrayList();

    // variable used for checking in which screen the user is
    private String screen = "main";

    // variable used to check if user is adding a new entry or editing
    private boolean editing = false;

    @FXML
    private void add(ActionEvent event) {

        // if the user selected a record for editing
        if (editing) {
            /* alert box to ask if user wants to forget about current search and
                add a new result */
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure"
                    + " you want to forget this Transaction and add a new one?",
                    ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Forget Search and Add New");
            alert.setHeaderText(null);

            // waits and gets the option chosen by the user
            Optional<ButtonType> result = alert.showAndWait();

            // if user pressed YES, reset all fields
            if (result.get() == ButtonType.YES) {
                clear(event);
                editing = false; // turn editing mode off
            }

        } else { // if not in editing mode

            // attempts to create a Transaction object
            try {

                // checks if date and value are empty to display proper message
                if (dpDate.getValue() == null) { // checks date
                    styleOutput(lblOutput, "error"); // changes style of label
                    lblOutput.setText("Invalid: Date cannot be empty.");
                    dpDate.requestFocus();

                } else if (txtValue.getText().equals("")) { // checks value
                    styleOutput(lblOutput, "error"); // changes style of label
                    lblOutput.setText("Invalid: Value cannot be empty.");
                    txtValue.requestFocus();

                } else { // if date and value are not empty
                    // attempts to create Transaction object
                    createTransaction();

                    // if successfull, clear all fields
                    clear(new ActionEvent());

                    // adds successfully created Transaction to the list
                    list.add(transaction);

                    // set style of message to success
                    styleOutput(lblOutput, "success"); // changes style of label

                    // show success message
                    lblOutput.setText("Transaction added successfully!");
                }

            } catch (NumberFormatException ex) { // problem parsing

                // set output style for error
                styleOutput(lblOutput, "error");

                /* exceptions caused for problems with parsing. In this case can 
                    only be thrown by the value since id is automatic */
                txtValue.requestFocus();
                lblOutput.setText("Invalid: Value must be a double.");

            } // problem creating object
            catch (IllegalArgumentException ex) {

                String error = ex.getMessage();

                // set output style for error
                styleOutput(lblOutput, "error");

                // display the error message
                lblOutput.setText(error);

                // search for which field is invalid and set focus on it
                if (error.contains("Date")) { // if caused by the date field
                    dpDate.requestFocus();
                } else if (error.contains("Category")) { // caused by category
                    cbxCategory.requestFocus();
                } else if (error.contains("Value")) { // caused by the value
                    txtValue.requestFocus();
                }
            }
        }
    }

    /**
     * Method that saves all items contained on the TransactionList to the file
     * and closes the application once complete.
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void exit(ActionEvent event) {

        // dialog box to confirm if user wishes to quit program
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit the program?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setTitle("Exit Program");

        // waits and gets user's answer
        Optional<ButtonType> result = alert.showAndWait();

        // if user wants to quit, list is saved into file
        if (result.get() == ButtonType.YES) {
            // reference to the text file where the data is stored
            File file = new File("transactions.txt");

            // reference to the text with the used ids
            File idsFile = new File("ids.txt");

            try {
                // open the output streams for saving the contents into the file
                PrintWriter fileOut = new PrintWriter(new BufferedWriter(
                        new FileWriter(file)));

                // add new line to file with all of the Transactions fields
                for (int i = 0; i < list.size(); i++) {
                    Transaction t = list.get(i);
                    fileOut.println(t.getId() + "|"
                            + String.valueOf(t.getDate()) + "|"
                            + t.getCategory().getName() + "|" + t.getMethod()
                            + "|" + String.format("%.2f", t.getValue()) + "|"
                            + t.getObservation());
                }

                // open output strem to file with ids
                PrintWriter fileIdsOut = new PrintWriter(new BufferedWriter(
                        new FileWriter(idsFile)));

                // add each id to the file
                for (int i = 0; i < ids.size(); i++) {
                    fileIdsOut.println(ids.get(i));
                }

                // closes the output streams
                fileOut.close();
                fileIdsOut.close();

            } catch (IOException ex) {

                // set output style for error
                styleOutput(lblOutput, "error");

                lblOutput.setText("There was a problem with the file.");
            }

            // exit program
            System.exit(0);
        }
    }

    /**
     * Method used for emptying all fields on the current screen.
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void clear(ActionEvent event) {
        if (screen.equals("main")) { // if on main screen

            // resets the id field to the next available
            txtID.setText(String.valueOf(ids.size()));

            // clear date and set category to first index
            dpDate.setValue(null);
            cbxCategory.getSelectionModel().selectFirst();

            // deselects whichever radio is selected
            optCash.setSelected(false);
            optDebit.setSelected(false);
            optCredit.setSelected(false);

            // clear value and observation
            txtValue.clear();
            txtObs.clear();

            // disables the edit and delete buttons
            btnEdit.setDisable(true);
            btnDelete.setDisable(true);

            // clear previous output messages
            lblOutput.setText("");

            // focus on the Date field
            dpDate.requestFocus();

            // change editing mode off
            editing = false;

        } else { // if on the search screen

            // empty all the fields
            txtIDS.clear();
            cbxCategoryS.getSelectionModel().selectFirst();
            dpDateS.setValue(null);
            cbxMethod.getSelectionModel().clearSelection();
            txtValueFrom.clear();
            txtValueTo.clear();
            dpDateFrom.setValue(null);
            dpDateTo.setValue(null);

            // clears old results on the ListView
            lstSearch.setItems(null);

            // disable edit button until another record is selected
            btnEditSearch.setDisable(true);
        }
    }

    /**
     * The method that changes from the add/edit page to the Search page when
     * the Search button on the first page is clicked.
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void changeScreen(ActionEvent event) {
        if (screen.equals("main")) {
            screen = "search"; // updates screen
            bpMain.setCenter(bpSearch); // sets the search screen

            // change width of the screen to fit every field
            bpMain.getScene().getWindow().setWidth(700);
        } else {
            screen = "main";
            bpMain.setCenter(grdMain); // sets to main screen

            // changes the width of the screen to look better
            bpMain.getScene().getWindow().setWidth(400);
        }
    }

    /**
     * Method that gets all the data from the fields, creates a new Transaction
     * and replace a Transaction at a certain index in the list by this new
     * Transaction created.
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void edit(ActionEvent event) {

        // index of the Transaction being edited
        int index = -1;

        try {
            // search for the Transaction matching the id
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == Integer.parseInt(txtID.getText())) {
                    index = i;
                    break; // get out of the loop
                }
            }

            if (index >= 0) { // if index was found

                if (txtValue.getText().equals("")) {
                    // set output style for error
                    styleOutput(lblOutput, "error");
                    lblOutput.setText("Invalid: Value cannot be empty.");
                } else if (dpDate.getValue() == null) {
                    // set output style for error
                    styleOutput(lblOutput, "error");
                    lblOutput.setText("Invalid: Date cannot be empty.");
                } else {
                    // attempts to create a Transaction object
                    createTransaction();

                    // replaces transaction at the index position
                    list.set(index, transaction);

                    // clear screen
                    clear(event);

                    // set output style for success
                    styleOutput(lblOutput, "success");

                    lblOutput.setText("Transaction edited successfully.");
                }

            } else { // if the transaction was not found

                // set output style for error
                styleOutput(lblOutput, "error");
                lblOutput.setText("Id not found.");
            }
        } catch (NumberFormatException ex) { // problem parsing

            // set output style for error
            styleOutput(lblOutput, "error");

            /* exceptions caused for problems with parsing. In this case can 
            only be thrown by the value */
            lblOutput.setText("Invalid: Value must be a number greater "
                    + "than zero.");
            txtValue.requestFocus();

        } catch (IllegalArgumentException ex) { // error creating object

            String error = ex.getMessage();

            // display the error message
            lblOutput.setText(error);

            // search for which field is invalid and set focus on it
            if (error.contains("Date")) { // if caused by the date field
                dpDate.requestFocus();
            } else if (error.contains("Category")) { // caused by category
                cbxCategory.requestFocus();
            } else if (error.contains("Value")) { // caused by the value
                txtValue.requestFocus();
            }
        }

        // turn off editing mode
        editing = false;
    }

    /**
     * Method that removes the Transaction with the specified id from the list.
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void delete(ActionEvent event) {

        // search for the Transaction matching the id
        int index = list.searchId(Integer.parseInt(txtID.getText()));

        if (index >= 0) { // if index was found

            // creates a new alert box with the YES and CANCEL option
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure"
                    + " you want to delete this Transaction?", ButtonType.YES,
                    ButtonType.CANCEL);
            alert.setTitle("Delete Transaction");
            alert.setHeaderText(null);

            // waits and gets the option chosen by the user
            Optional<ButtonType> result = alert.showAndWait();

            // if user pressed YES, delete record and clear fields
            if (result.get() == ButtonType.YES) {

                // clear screen
                clear(event);

                // set output style for success
                styleOutput(lblOutput, "success");

                // remove record and show message
                list.remove(index);
                lblOutput.setText("Record deleted successfully");

            }
        } else { // if object was not found

            // set output style for error
            styleOutput(lblOutput, "error");

            lblOutput.setText("Invalid: The object with the id searched was not"
                    + " found");
        }
    }

    /**
     * Method that check which search has to be made, call the correct method to
     * search the Transactions in the list and show the objects found in the
     * ListView.
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void display(ActionEvent event) {

        // clears old results on the ListView
        lstSearch.setItems(null);

        // disables editing button until something new is selected
        btnEditSearch.setDisable(true);

        // array with all the option buttons to check wath is being searched
        RadioButton[] options = {optID, optCategory, optDate, optMethod,
            optValue, optDateRange};

        // initialize option's index selected
        int option = -1;

        // checks to see which radio button is selected
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                option = i; // assign index of selected
                break;
            }
        }

        // array that will contain the indexes of the items found
        int[] indexes = new int[1];

        try {
            // based on the index, the right method is called
            switch (option) {
                case 0: // search by id

                    // if field is empty
                    if (txtIDS.getText().equals("")) {
                        throw new IllegalArgumentException("Invalid: ID cannot "
                                + "be empty");

                    } else { // if field is not empty
                        int index = list.searchId(Integer.parseInt(txtIDS.
                                getText()));

                        if (index != -1) { // if index is -1, it was not found
                            indexes[0] = index;
                        } else { // if not found, sets size of array to 0
                            indexes = new int[0];
                        }
                    }
                    break;

                case 1: // search by category

                    // if no category was selected throw exception
                    if (cbxCategoryS.getSelectionModel().
                            getSelectedIndex() == 0) {
                        throw new IllegalArgumentException("Invalid: Please "
                                + "select a category");
                    }

                    indexes = list.searchCategory(cbxCategoryS.
                            getSelectionModel().getSelectedItem());
                    break;
                case 2: // search by date
                    indexes = list.searchDate(dpDateS.getValue());
                    break;
                case 3: // search by method
                    indexes = list.searchMethod(cbxMethod.
                            getSelectionModel().getSelectedItem());
                    break;
                case 4: // search by value range

                    // if fields are empty
                    if (txtValueFrom.getText().equals("")
                            || txtValueTo.getText().equals("")) {
                        throw new IllegalArgumentException("Invalid: Values "
                                + "cannot be empty.");
                    } else {
                        double min = Double.parseDouble(txtValueFrom.getText());
                        double max = Double.parseDouble(txtValueTo.getText());

                        indexes = list.searchValueRange(min, max);
                    }
                    break;
                case 5: // search by date range
                    LocalDate from = dpDateFrom.getValue();
                    LocalDate to = dpDateTo.getValue();

                    indexes = list.searchDateRange(from, to);
                    break;
                default: // if no option was selected (option is -1)
                    throw new IllegalArgumentException("Invalid: An option "
                            + "should be selected.");
            }

            // since search was successful create observable list
            ObservableList<Transaction> obsTransactionList = FXCollections.
                    observableArrayList();

            // if no elements in index, no results were found
            if (indexes.length == 0) {

                // clean previous total value output
                lblTotal.setText("");

                // sets style of output to error and displays message
                styleOutput(lblOutputSearch, "error");
                lblOutputSearch.setText("No results were found.");
            } else { // if any results were found

                double sumValue = 0;

                // add all Transactions found in the search
                for (int i = 0; i < indexes.length; i++) {

                    // add value to sum
                    sumValue += list.get(indexes[i]).getValue();

                    // add record to list
                    obsTransactionList.add(list.get(indexes[i]));
                }

                // sets style of output to success
                styleOutput(lblOutputSearch, "success");

                // display message
                if (indexes.length > 1) {
                    lblOutputSearch.setText(String.format("%d Results were "
                            + "found", indexes.length));
                } else {
                    lblOutputSearch.setText("1 Result found");
                }

                // displays sum of values of results found
                lblTotal.setText(String.format("%.2f", sumValue));
            }

            // assign the observable list to the ViewList
            lstSearch.setItems(obsTransactionList);

        } catch (NumberFormatException ex) { // wrong parsing from fields

            // sets style of output to error and displays message
            styleOutput(lblOutputSearch, "error");

            // clean previous total value output
            lblTotal.setText("");

            if (optValue.isSelected()) { // if caused by value
                lblOutputSearch.setText("Invalid: Values must be doubles.");
                txtValueFrom.requestFocus();
            } else { // if caused by id
                lblOutputSearch.setText("Invalid: Id must be integer.");
                txtIDS.requestFocus();
            }

        } catch (IllegalArgumentException ex) {

            // clean previous total value output
            lblTotal.setText("");

            // sets style of output to error and displays message
            styleOutput(lblOutputSearch, "error");
            lblOutputSearch.setText(ex.getMessage());
        }
    }

    /**
     * Method that gets the record (Transaction) selected on the ListView and
     * insert each of its fields into the main screen's fields, making it
     * possible for the user to change any of the data or delete the record.
     *
     * @param event the event object fired by the button
     */
    public void selectRecord(ActionEvent event) {
        Transaction selected = lstSearch.getSelectionModel().getSelectedItem();

        if (selected == null) {
            // sets style of output to error and displays message
            styleOutput(lblOutputSearch, "error");
            lblOutputSearch.setText("Invalid: no Transaction was selected.");
        } else { // if a record was selected
            txtID.setText(String.valueOf(selected.getId())); // set id
            dpDate.setValue(selected.getDate()); // set date

            // set Category
            cbxCategory.getSelectionModel().
                    select(selected.getCategory().getName());

            // set Method
            if (selected.getMethod().equals("Cash")) {
                optCash.setSelected(true);
            } else if (selected.getMethod().equals("Debit")) {
                optDebit.setSelected(true);
            } else if (selected.getMethod().equals("Credit")) {
                optCredit.setSelected(true);
            } else { // if it does not match any option (error)

                // set output style for error
                styleOutput(lblOutputSearch, "error");

                lblOutput.setText("Invalid: Transaction selected has an "
                        + "invalid method value.");
            }

            // set formated value
            txtValue.setText(String.format("%.2f", selected.getValue()));

            // set observation if not equal to NA
            if (!selected.getObservation().equals("NA")) {
                txtObs.setText(selected.getObservation());
            }

            // enables edit and delete buttons
            btnEdit.setDisable(false);
            btnDelete.setDisable(false);

            // change screens to the main one
            changeScreen(new ActionEvent());

            // clear any previous output message
            lblOutput.setText("");

            // change variable for editing mode
            editing = true;
        }
    }

    /**
     * Method used for enabling the field related to the RadioButton clicked for
     * the Search screen
     *
     * @param event the event object fired by the button
     */
    @FXML
    private void selectFilter(ActionEvent event) {

        // Parallel arrays for matching a radio button to its respective field
        RadioButton[] options = {optID, optCategory, optDate, optMethod,
            optValue, optDateRange};
        Node[] disabled = {txtIDS, cbxCategoryS, dpDateS, cbxMethod,
            txtValueFrom, dpDateFrom};

        // first, disable (reset) all previouslly enabled fields
        for (Node f : disabled) {
            f.setDisable(true);
        }

        // disable value to and date to
        txtValueTo.setDisable(true);
        dpDateTo.setDisable(true);

        // goes through all radio buttons to see which field to enable
        for (int i = 0; i < options.length; i++) {

            // matches the radio array with the trigger of the event
            if (event.getSource() == options[i]) {
                disabled[i].setDisable(false); // enable field

                // if value was selected, enable the Value to
                if (i == 4) {
                    txtValueTo.setDisable(false);
                } // if date range was selected, enable Date to
                else if (i == 5) {
                    dpDateTo.setDisable(false);
                }
            }
        }
    }

    /**
     * Method that searches for which method's Radio Button is selected and
     * returns a String of the option selected
     *
     * @return a String with the name of the method selected
     */
    private String getMethodSelected() {
        if (optCash.isSelected()) { // check Cash option
            return "Cash";
        } else if (optDebit.isSelected()) { // check Debit option
            return "Debit";
        } else if (optCredit.isSelected()) { // check Credit option
            return "Credit";
        } else { // throw exception if none is selected
            throw new IllegalArgumentException("Invalid: One of the method's"
                    + " option must be selected.");
        }
    }

    /**
     * Method used for creating a new Transaction object based on the data
     * contained on the fields on the main screen.
     */
    private void createTransaction() {

        // gets all data from fields
        int id = Integer.parseInt(txtID.getText());
        LocalDate date = dpDate.getValue();
        Category category = Category.getCategory(cbxCategory.
                getSelectionModel().getSelectedItem());
        String method = getMethodSelected();
        double value = Double.parseDouble(txtValue.getText());
        String obs = txtObs.getText();

        // constructs the transaction object
        transaction = new Transaction(id, date, category,
                method, value, obs);

        // if not on edit mode
        if (!editing) {
            // adds ID to the id list
            ids.add(id);
        }
    }

    /**
     * Method used to change the style of message's output from success to error
     * or vice versa.
     *
     * @param output the output label that needs to be styled
     * @param style String of the name of the style wanted
     */
    private void styleOutput(Label output, String style) {
        if (style.equals("error")) {
            output.getStyleClass().remove("success");
            output.getStyleClass().add(style);
        } else {
            output.getStyleClass().remove("error");
            output.getStyleClass().add(style);
        }

    }

    /**
     * Method that is executed when the application is started. It reads the
     * text files and saves all of its contents into the correct arrays. It also
     * initializes all ComboBoxes's contents, it sets the correct ID into the ID
     * TextField, it contains the event handler for the ListView and for the
     * window exit button.
     *
     * @param url the location used to resolve relative paths for root object
     * @param rb resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // reference the file with all the Transactions
        File file = new File("transactions.txt");

        if (file.exists()) {

            try {
                // initialize the list object with all Transactions on file
                list = new TransactionList(file);
            } catch (FileNotFoundException ex) { // file does not exist

                // change style for error and display error
                styleOutput(lblOutput, "error");
                lblOutput.setText("Invalid: File was not found.");
            } catch (NumberFormatException ex) { // error parsing

                // change style for error and display error
                styleOutput(lblOutput, "error");
                lblOutput.setText("Invalid: One or more fields in the file has "
                        + "invalid data type.");
            } catch (IllegalArgumentException ex) { // error creating object

                // change style for error and display error
                styleOutput(lblOutput, "error");
                lblOutput.setText(ex.getMessage());
            } catch (Exception ex) { // unknown error

                // change style for error and display error
                styleOutput(lblOutput, "error");
                lblOutput.setText("Error: Please contact support.");
            }
        }

        // reference the file with all the used ids
        File idsFile = new File("ids.txt");

        try {
            if (idsFile.exists()) {

                // open stream
                Scanner in = new Scanner(idsFile);

                // place each id into the array list ids
                while (in.hasNextLine()) {
                    int id = Integer.parseInt(in.nextLine());
                    ids.add(id);
                }
            }
        } catch (IOException ex) { // problem with the file
            styleOutput(lblOutput, "error");
            lblOutput.setText("Invalid: File for IDs was not found.");
        } catch (NumberFormatException ex) { // problem parsing the id
            styleOutput(lblOutput, "error");
            lblOutput.setText("Invalid: One of the IDs in the file is not an"
                    + " integer.");
        } catch (Exception ex) { // unkown error
            styleOutput(lblOutput, "error");
            lblOutput.setText("Error: Please contact support.");
        }

        // String array that will hold all Category's names
        String[] strCategory = new String[Category.SIZE + 1];

        // first element will be a default placeholder
        strCategory[0] = "--Select Category--";

        // add each Category's name to the array
        for (Category c : Category.values()) {
            strCategory[c.ordinal() + 1] = c.getName();
        }

        // observable list with the Category's names
        ObservableList<String> obsCat = FXCollections.
                observableArrayList(strCategory);

        // assign the ComboBox Category to its observable list of items
        cbxCategory.setItems(obsCat);
        cbxCategoryS.setItems(obsCat);

        // select first as default
        cbxCategory.getSelectionModel().selectFirst();
        cbxCategoryS.getSelectionModel().selectFirst();

        // if file is still empty (first entry)
        if (ids.isEmpty()) {
            txtID.setText("0");
        } else { // if file is not empty

            // initiates the id field as 1 more than the size of the id array
            txtID.setText(String.valueOf(ids.size()));
        }

        // add the three possibilities of methods to the Method comboBox
        String[] methods = {"Cash", "Debit", "Credit"};

        // observable list with the methods
        ObservableList<String> obsMethods = FXCollections.
                observableArrayList(methods);

        // assign the ComboBox Method to its observable list of items
        cbxMethod.setItems(obsMethods);

        // add event listener for the list, enabling the button for editing
        lstSearch.getSelectionModel().selectedItemProperty().
                addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {

                        // if something was selected, enable editing button
                        btnEditSearch.setDisable(false);
                    }
                });

        /* make it possible for running the exit method if there is an attempt
            of closing the window */
        Platform.runLater(new Runnable() {

            // abstract method of Runnable
            public void run() {

                // gets the value of the property Window 
                Window win = btnExit.getScene().getWindow();

                /* adds event handler for when there is an attempt to close the
                    window by an external request */
                win.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    // abstract method of EventHandler
                    @Override
                    public void handle(WindowEvent event) {

                        // calls the exit method
                        exit(new ActionEvent());

                        /* if exit method does not close the application (if 
                            user pressed NO) stops event handler */
                        event.consume();
                    }
                });
            }
        });
    }
}
