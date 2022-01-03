package org.prog3.project.muppetsmail.Client.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.prog3.project.muppetsmail.Client.ClientApp;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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
                Object lock = new Object();

                appModel.connectionManager.runTask(Constants.COMMAND_DELETE_MAIL, lock, mail);
                synchronized (lock){
                    try {
                       
                        lock.wait();
                        appModel.getCurrentMailFolder().remove(mail);
                        
                    } catch (InterruptedException  e) {
                        e.printStackTrace();
                    }
                }

                Stage stage = (Stage) showMailFrom.getScene().getWindow();
                stage.close();

            }
        });

        replyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("MailComposer.fxml"));
                try {
                    Stage stage = new Stage();
                    stage.setScene(loader.load());
                    stage.setTitle("Reply to email - Muppets Mail Client");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(Objects.requireNonNull(ClientApp.class.getResourceAsStream("ClientIcon.png"))));
                    MailComposerController mailComposerController = loader.getController();
                    mailComposerController.setClientModel(appModel);
                    mailComposerController.setReplyEmail(mail);

                    stage.show();

                    Stage viewStage = (Stage) replyButton.getScene().getWindow();
                    viewStage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        forwardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent){
                FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("MailComposer.fxml"));
                try {
                    Stage stage = new Stage();
                    stage.setScene(loader.load());
                    stage.setTitle("Forward email - Muppets Mail Client");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image(Objects.requireNonNull(ClientApp.class.getResourceAsStream("ClientIcon.png"))));
                    MailComposerController mailComposerController = loader.getController();
                    mailComposerController.setClientModel(appModel);
                    mailComposerController.setForwardMail(mail);

                    stage.show();

                    Stage viewStage = (Stage) replyButton.getScene().getWindow();
                    viewStage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
