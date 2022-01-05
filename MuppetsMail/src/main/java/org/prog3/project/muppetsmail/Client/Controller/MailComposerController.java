package org.prog3.project.muppetsmail.Client.Controller;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.util.ArrayList;
import java.util.Arrays;

public class MailComposerController {

    /*
     * UI Vars
     *  */
    public TextField mailSubject;
    public TextField mailToRecipients;
    public Button mailSendButton;
    public TextArea mailBodyViewer;

    private ClientModel appModel;

    public void setReplyEmail(Mail replyTo){
        mailSubject.setText("Re:" + replyTo.getSubject());
        mailBodyViewer.setText("\n----------------------\n" + replyTo.getMessage());
        String tmpRecipiants = replyTo.getFrom() + ";" + replyTo.getTo().toString();

        tmpRecipiants = tmpRecipiants.replace("[", "");
        tmpRecipiants = tmpRecipiants.replace("]", "");
        tmpRecipiants = tmpRecipiants.replace(",", ";");
        tmpRecipiants = tmpRecipiants.replace(appModel.getUsername().get(), "");
        tmpRecipiants = tmpRecipiants.replace(";;", ";");

        mailToRecipients.setText(tmpRecipiants);
    }

    public void setForwardMail(Mail replyTo){
        mailSubject.setText("FWD:" + replyTo.getSubject());
        mailBodyViewer.setText("\n----------------------\n" + replyTo.getMessage());
    }

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

            Mail mailToBeSent = new Mail( appModel.getUsername().getValue(), recipients, mailBody, mailSubject, Constants.MAILBOX_SENT_FOLDER);

            Object lock = new Object();

            appModel.connectionManager.runTask(Constants.COMMAND_SEND_MAIL, lock, mailToBeSent);
            synchronized (lock){
                try {
                    lock.wait();
                    Stage stage = (Stage) mailSendButton.getScene().getWindow();
                    stage.close();

                } catch (InterruptedException  e) {
                    e.printStackTrace();
                }
            }


        });
    }
}
