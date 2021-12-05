package org.prog3.project.muppetsmail.Client.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import java.net.URL;
import java.util.ResourceBundle;

public class MailViewerController implements Initializable {
    /*
    * UI vars
    * */
    public Label showMailFrom;
    public Label showMailTo;
    public Label showMailSubject;
    public Button replyButton;
    public Button forwardButton;
    public Button deleteMailButton;
    public TextArea messageBodyDisplay;
    private Mail mail;
    private ClientModel appModel;

    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
    }
    public void setMail(Mail mail){ this.mail = mail;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteMailButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                synchronized (mail) {
                    
                    mail.setCurrentMailBox(3);
                    //TODO: DELETE EMAIL
                }
                Stage stage = (Stage) showMailFrom.getScene().getWindow();
                stage.close();

            }
        });
    }
}
