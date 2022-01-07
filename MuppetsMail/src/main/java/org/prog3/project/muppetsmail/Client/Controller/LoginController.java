package org.prog3.project.muppetsmail.Client.Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Utils;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the controller for the login view
 */
public class LoginController implements Initializable {

    /*
     * UI vars
     */
    public TextField serverInput;
    public TextField portInput;
    public TextField usernameInput;
    public Button loginButton;
    private ClientModel appModel;

    /**
     * This function sets the clientModel, effectively acting as a constructor for the class, 
     * as the constructor cannot be called due to it being controlled by FXMLLoader
     * @param clientModel the client model
     */
    public void setClientModel(ClientModel clientModel) {
        this.appModel = clientModel;
        this.appModel.getUsername().bind(this.usernameInput.textProperty());
        this.appModel.getEndpoint().bind(this.serverInput.textProperty());
        this.appModel.getEndpointPort().bind(this.portInput.textProperty());
    }

    /**
     * This function is called by the FXMLLoader once the class is constructed.
     * it initializes events on the interface wich do not require a model to be set
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(actionEvent -> login() );
    }

    /**
     * This function checks the connection to be valid. if so, the login view is hidden and the status of the model is changed to logged, 
     * so that the listener set in the home view controller can populate the listeners and ui items
     */
    private void login(){
        if (!serverInput.getText().equals("") && !portInput.getText().equals("") && !usernameInput.getText().equals("")) {
            //we create a new connectionManager in the model.
            appModel.connectionManager = new ConnectionManager(serverInput.getText(),Integer.parseInt(portInput.getText()), appModel);

            if (appModel.connectionManager.connectToServer()) {
                appModel.getClientIsLogged().set(true);
                
                //fetch of inbox if the client is logged
                Object lock = new Object();
                appModel.connectionManager.runTask(Constants.COMMAND_FETCH_INBOX, lock);
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

                //hide of login show
                ((Stage) loginButton.getScene().getWindow()).close();;

            } else {
                Utils.showAlert(AlertType.ERROR, "Cannot establish connection with server", "Connection error", "Error");
            }
            
        } else {
            showErrorEmpyFields();
        }
    }

    /**
     * This function show an error message in case some fileds in the login stage are not filled
     */
    public void showErrorEmpyFields(){
        String MissingFields = "";
        if (serverInput.getText().equals(""))
            MissingFields += "Server Endpoint\n\t";
        if (portInput.getText().equals(""))
            MissingFields += "Enpoint port\n\t";
        if (usernameInput.getText().equals(""))
            MissingFields += "Username\n\t";

        Utils.showAlert(AlertType.ERROR, "Warning: the following fields are empty:\n\t" + MissingFields + "\nComplete them and retry!", "Connection error", "Error");

    }

}
