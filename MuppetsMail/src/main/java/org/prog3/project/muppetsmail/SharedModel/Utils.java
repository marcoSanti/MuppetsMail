package org.prog3.project.muppetsmail.SharedModel;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

    /**
     * this function shows an alert message
     * @param type alert type
     * @param message message to show
     * @param header header of the message
     * @param title title of the message window
     */
    public static void showAlert(AlertType type, String message, String header, String title){
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                Alert alert = new Alert(type, message);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.show();   
            }
        }); 
    }
    
}
