package org.prog3.project.muppetsmail.SharedModel;

import org.prog3.project.muppetsmail.Server.Model.ServerModel;

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

    /**
     * This function adds a log message to the gui
     * @param message description message
     * @param serverModel the server model
     */
    public static void addLogToGUI(String message, ServerModel serverModel) {
        Utils.addLogToGUI(message, "", serverModel);
    }

    /**
     * This function adds a log message to the gui
     * @param message description message
     * @param detailed detailed description message
     * @param serverModel the server model
     */
    public static void addLogToGUI(String message, String detailed, ServerModel serverModel) {
        Platform.runLater(new Runnable() { // done because a new task is require to update model and gui
            @Override
            public void run() {
                serverModel.addLog(message, detailed);
            }
        });
    }
    
}
