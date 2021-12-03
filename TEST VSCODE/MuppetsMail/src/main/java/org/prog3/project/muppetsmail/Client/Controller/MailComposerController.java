package org.prog3.project.muppetsmail.Client.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.Client.Model.Constants;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.lang.reflect.Array;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class MailComposerController {

    /*
     * UI Vars
     *  */
    public TextField mailSubject;
    public TextField mailToRecipients;
    public Button mailSendButton;
    public TextArea mailBodyViewer;

    private ClientModel appModel;


    public void setClientModel(ClientModel clientModel) {
        this.appModel = clientModel;
        mailSendButton.setOnAction(actionEvent -> {
            String mailSubject = this.mailSubject.getText();
            String mailToRecipients = this.mailToRecipients.getText();
            String mailBody = this.mailBodyViewer.getText();

            if (mailToRecipients.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No recipients for email! please insert at leas one recipient!");
                alert.setTitle("No recipients error");
                alert.setHeaderText("Error");
                alert.show();
                return;
            }
            if (mailSubject.equals("")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "A subject for an email was not inserted.\nAre you sure you want to send the email anyway?");
                alert.setResizable(false);
                alert.setTitle("Empty email subject");
                alert.setHeaderText("Warning");

                if (alert.showAndWait().get() != ButtonType.OK) {
                    return;
                }
            }


            ArrayList<String> recipients = new ArrayList<>(Arrays.asList(mailToRecipients.split(";")));

            Mail mailToBeSent = new Mail(String.valueOf(mailBody.hashCode()), appModel.getUsername().getValue(), recipients, mailBody, mailSubject, Constants.MAILBOX_SENT_FOLDER);

            Object lock = new Object();

            appModel.connectionManager.runTask(Constants.COMMAND_SEND_MAIL, lock, mailToBeSent);
            synchronized (lock){
                try {
                    lock.wait();
                    appModel.getUserMailBox().addMail(mailToBeSent, Constants.MAILBOX_SENT_FOLDER);
                    Stage stage = (Stage) mailSendButton.getScene().getWindow();
                    stage.close();

                } catch (InterruptedException  e) {
                    e.printStackTrace();
                }
            }


        });
    }
}