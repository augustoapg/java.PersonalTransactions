/*
    Name: Augusto Araujo Peres Goncalez
    Assignment: Final Project
    Program: Computer Systems Technology – Systems Analyst
    Date: 08/16/17

    Description:
    This application is used for a user to store personal expenses as 
    Transaction objects, which contains fields for storing an auto assigned id,
    a date, a Category, a value, the method of payment, and an optional 
    observation. Those objects are stored in a sequential file, and each
    record can be displayed at will by selecting the filters for searching the 
    Transactions that match the search. The app also allows the user to edit and
    delete any old records. 
 */
package araujope;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class of the application
 *
 * @author Augusto Araujo Peres Goncalez
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().
            getResource("FXMLFinalProject.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Personal Finances");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method of the application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
