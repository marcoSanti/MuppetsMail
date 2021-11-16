package org.prog3.project.muppetsmail.Client.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    /*
    * UI vars
    * */
    public TextField serverInput;
    public TextField portInput;
    public TextField usernameInput;
    public Button loginButton;
    private ClientModel appModel;

    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
        this.appModel.getUsername().bind(this.usernameInput.textProperty());
        this.appModel.getEndpoint().bind(this.serverInput.textProperty());
        this.appModel.getEndpointPort().bind(this.portInput.textProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!serverInput.getText().equals("") && !portInput.getText().equals("") && !usernameInput.getText().equals("")){
                    appModel.getClientIsLogged().set(true);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                }else{
                    String MissingFields = "";
                    if(serverInput.getText().equals("")) MissingFields += "Server Endpoint\n\t";
                    if(portInput.getText().equals("")) MissingFields += "Enpoint port\n\t";
                    if(usernameInput.getText().equals("")) MissingFields += "Username\n\t";

                    Alert alert = new Alert(Alert.AlertType.ERROR, "Warning: the following fields are empty:\n\t" + MissingFields + "\nComplete them and retry!");
                    alert.show();
                }
            }
        });


    }
}
