package org.prog3.project.muppetsmail.Client.Controller;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the controller for the mail composer
 */

public class MailComposerController {

    /*
     * UI Vars
     */

    public TextField mailSubject;
    public TextField mailToRecipients;
    public Button mailSendButton;
    public TextArea mailBodyViewer;

    private ClientModel appModel;

    /**
     * This function is used to create the frame to a response email. it updates the subject of the
     * email, sets the destination addresses as the one your email came plus the one of the others recipients
     * it is used by the mailViewer
     * @param replyTo the email you wish to reply to
     */
    public void setReplyEmail(Mail replyTo){

        mailSubject.setText("Re:" + replyTo.getSubject());
        mailBodyViewer.setText("\n----------------------\n" + replyTo.getMessage());

        //preparing destination addresses
        String tmpRecipiants = replyTo.getFrom() + ";" + replyTo.getTo().toString();
        tmpRecipiants = tmpRecipiants.replace("[", "");
        tmpRecipiants = tmpRecipiants.replace("]", "");
        tmpRecipiants = tmpRecipiants.replace(",", ";");
        tmpRecipiants = tmpRecipiants.replace(appModel.getUsername().get(), ""); //remove of own username from destination addreses
        tmpRecipiants = tmpRecipiants.replace(";;", ";");

        mailToRecipients.setText(tmpRecipiants);
    }

    /**
     * This function is used to forward an email. it is used by the homeView to
     * allow for a easy forward of the email
     * it sets the email subject and the email body
     * @param forwardEmail the email to forward
     */
    public void setForwardMail(Mail forwardEmail){
        mailSubject.setText("FWD:" + forwardEmail.getSubject());
        mailBodyViewer.setText("\n----------------------\n" + forwardEmail.getMessage());
    }

    /***
     * This function is used to set the client model and is used as a contructor to set up behaviour for
     * items that requires the model to not be null
     * @param clientModel the client app model
     */
    public void setClientModel(ClientModel clientModel) {
        this.appModel = clientModel;

        mailSendButton.setOnAction(actionEvent -> mailSendHandler() );
    }


    /**
     * This function send an email. it controls that the destinations address is not empty and
     * that also the subject of email is not empty
     */
    private void mailSendHandler(){
        String mailSubject = this.mailSubject.getText();
        String mailToRecipients = this.mailToRecipients.getText();
        String mailBody = this.mailBodyViewer.getText();

        if (mailToRecipients.equals("")) {
            Utils.showAlert(AlertType.ERROR, "No recipients for email! please insert at leas one recipient!", "Error", "No recipients error");
            return;
        }
        if (mailSubject.equals("")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "A subject for an email was not inserted.\nAre you sure you want to send the email anyway?");
            alert.setResizable(false);
            alert.setTitle("Empty email subject");
            alert.setHeaderText("Warning");

            //if i press ok i will send email otherwise no action is performed
            if (alert.showAndWait().get() != ButtonType.OK)  return;
            
        }

        //generate the new email
        ArrayList<String> recipients = new ArrayList<>(Arrays.asList(mailToRecipients.split(";")));
        Mail mailToBeSent = new Mail( appModel.getUsername().getValue(), recipients, mailBody, mailSubject, Constants.MAILBOX_SENT_FOLDER);

        Object lock = new Object();
        appModel.connectionManager.runTask(Constants.COMMAND_SEND_MAIL, lock, mailToBeSent);
        synchronized (lock){
            try {
                
                lock.wait();
                ((Stage) mailSendButton.getScene().getWindow()).close();

            } catch (InterruptedException  e) {
                System.out.println(e.getMessage());
            }
        }


    }
}
