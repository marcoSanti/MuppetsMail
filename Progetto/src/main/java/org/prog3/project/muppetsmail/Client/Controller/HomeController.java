package org.prog3.project.muppetsmail.Client.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    /*
    * UI Vars
    * */

    public Label usernameLabel;
    public Label serverLabel;
    public Button disconnectButton;
    public Button logOutButton;
    public Circle connectionStatusCircle;
    public Button sentButton;
    public Button inboxButton;
    public Button trashButton;
    public Button createNewMessageButton;
    public ListView<Mail> listViewMessages;
    private ClientModel appModel;
    private Stage loginStage;

    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
        usernameLabel.textProperty().bind(appModel.getUsername());
        serverLabel.textProperty().bind(appModel.getEndpoint());

        clientModel.getClientIsLogged().addListener(
                (observableValue, aBoolean, t1) -> toggleLoginLogOutButton()
        );


        appModel.getClientIsLogged().addListener(
                (observableValue, oldVal, newVal) -> {
                            if((appModel != null) && newVal){
                                connectionStatusCircle.setFill(Color.LAWNGREEN);
                                listViewMessages.setItems(appModel.getUserMailBox().getInbox());

                                //TODO: super bug: se mi loggo con user esistente e poi mi locco con user non esistente le mail sono le stesse di quello di prima... capirte come mai

                                inboxButton.setOnAction(actionEvent -> listViewMessages.setItems(appModel.getUserMailBox().getInbox()));
                                trashButton.setOnAction(actionEvent -> listViewMessages.setItems(appModel.getUserMailBox().getDeleted()));
                                sentButton.setOnAction(actionEvent -> listViewMessages.setItems(appModel.getUserMailBox().getSent()));


                            }else { //if disconnected or not yet connected
                                listViewMessages.setItems(null);
                                sentButton.setOnAction(null);
                                trashButton.setOnAction(null);
                                inboxButton.setOnAction(null);
                                connectionStatusCircle.setFill(Color.RED);
                            }
                }
        );
    }

    public void setLoginStage (Stage lStage){this.loginStage = lStage;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        connectionStatusCircle.setFill(Color.RED);

        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                appModel.getClientIsLogged().setValue(false);
                loginStage.show();
            }
        });

    }

    private void toggleLoginLogOutButton(){
        if(appModel.getClientIsLogged().getValue()){
            logOutButton.setText("Log out");
        }else{
            logOutButton.setText("Log in");
        }
    }



}
