package org.prog3.project.muppetsmail.Client.Controller;

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

/**
 * This class is the controller for the MailViewer view
 */
public class MailViewerController implements Initializable {
    /*
    * UI vars
    */
    public Label showMailFrom;
    public Label showMailTo;
    public Label showMailSubject;
    public Button replyButton;
    public Button forwardButton;
    public Button deleteMailButton;
    public TextArea messageBodyDisplay;
    private Mail mail;
    private ClientModel appModel;

    /**
     * This function sets the client app model, it is required to be able to delete an email
     * @param clientModel the client app model
     */
    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
    }

    /**
     * This function sets the email to be shown
     * @param mail the email to be shown
     */
    public void setMail(Mail mail){ 
        this.mail = mail;
    }

    /**
     * This function is used to add the event listener to the buttons
     * of the graphical interface
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteMailButton.setOnAction( actionEvent -> deleteMail() );
        replyButton.setOnAction( actionEvent -> replyToEmail() );
        forwardButton.setOnAction( actionEvent -> forwardEmail() );
    }


    /**
     * this function deletes an email from the server, and then removes it from the shown email
     * (it is moved to the trash folder)
     */
    private void deleteMail(){

        Object lock = new Object();
        appModel.connectionManager.runTask(Constants.COMMAND_DELETE_MAIL, lock, mail);
        
        try {
            synchronized (lock){
            lock.wait();
            }
            appModel.getCurrentMailFolder().remove(mail);
    
        } catch (InterruptedException  e) {
            System.out.println(e.getMessage());
        }
        ((Stage) showMailFrom.getScene().getWindow()).close();
    }

    /**
     * This function is used to forward an email
     */
    private void forwardEmail(){
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

            ((Stage) replyButton.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is udes to reply to an email
     */
    private void replyToEmail(){
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

            ((Stage) replyButton.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
