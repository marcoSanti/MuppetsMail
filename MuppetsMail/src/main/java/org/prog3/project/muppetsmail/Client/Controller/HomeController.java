package org.prog3.project.muppetsmail.Client.Controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.ClientApp;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.Client.ViewObjs.CellFactory;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is the controller for the home view
 */
public class HomeController implements Initializable {

    /*
    * UI Vars
    */
    public Label usernameLabel;
    public Label serverLabel;
    public Button logOutButton;
    public Circle connectionStatusCircle;
    public Button sentButton;
    public Button inboxButton;
    public Button trashButton;
    public Button createNewMessageButton;
    public Button refreshMailbox;
    public ListView<Mail> listViewMessages;
    private ClientModel appModel;
    private Stage loginStage;


    /**
     * This is the initialize method that is called when the FMXLLoader iniitalize the controller.
     * it only sets up things that do not require a model to be set, suc as filling the status circle,
     * and setting up the cellFactory for the listView, and finally, add a handler to the logout button
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        connectionStatusCircle.setFill(Color.RED);
        listViewMessages.setCellFactory(mailListView -> new CellFactory(appModel));

        logOutButton.setOnAction(actionEvent ->{
                appModel.getClientIsLogged().setValue(false);
                loginStage.show();
            });
    }

    /**
     * this function is used to set the client model to the controller.
     * it is effecevly used as a constructor, as we have no way to manage the momento in wich the
     * class is being initialized, due to being managed by FXMLLoader.
     * In this function, we also set the action to various ui elements, as they require 
     * the model to not be null.
     * @param clientModel the client app model
     */
    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;

        /*
        update username and server label by binding 
        it to the clientModel username and server property
        */
        usernameLabel.textProperty().bind(appModel.getUsername());
        serverLabel.textProperty().bind(appModel.getEndpoint());

        /**
         * adding a listener so that once the client is logged, 
         * the main view og the app is populated, and when a user is logged out, 
         * the view is emptied and the actions on the buttons are removed.
         * Also the login button is changed accordingly to the status of the user
         */
        appModel.getClientIsLogged().addListener( (observableValue, oldVal, newVal) -> {
                toggleLoginLogOutButton();
                if((appModel != null) && newVal){
                    setHomeElements();
                }else { 
                    clearHomeElements();
                }
            }
        );
    }

    /**
     * This function pulls the message in the current shown mail box from the server.
     * This is done by using the currentMailBox value as the command to send to the server
     */
    public void refreshMailbox() {
        Object lock = new Object();
       
        appModel.connectionManager.runTask(appModel.getCurrentMailBoxFolder(), lock);
        try {
            synchronized (lock){
                lock.wait();
            }
            listViewMessages.setItems(appModel.getCurrentMailFolder());  
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * This function is used by the main app to pass the pointer to the login stage, so that
     * once we logo out we are able to show that stage without creating a new one
     * @param lStage the login stage 
     */
    public void setLoginStage (Stage lStage){
        this.loginStage = lStage;
    }

    /**
     * This function remove all the listeners on the buttons of the home view
     */
    private void clearHomeElements(){
        listViewMessages.setItems(null);
        sentButton.setOnAction(null);
        trashButton.setOnAction(null);
        inboxButton.setOnAction(null);
        refreshMailbox.setOnAction(null);
        createNewMessageButton.setOnAction(null);
        connectionStatusCircle.setFill(Color.RED);
    }

    /**
     * This function sets the listeners for the home view elements
     */
    private void setHomeElements() {
        connectionStatusCircle.setFill(Color.LAWNGREEN);
        listViewMessages.setItems(appModel.getCurrentMailFolder());

        listViewMessages.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()==2) showMailInfo(); 
        });

        inboxButton.setOnAction(actionEvent -> updateCurrentMailFolder(Constants.COMMAND_FETCH_INBOX) );
        trashButton.setOnAction(actionEvent -> updateCurrentMailFolder(Constants.COMMAND_FETCH_DELETE) );
        sentButton.setOnAction(actionEvent -> updateCurrentMailFolder(Constants.COMMAND_FETCH_SENT) );
        refreshMailbox.setOnAction(actionEvent -> this.refreshMailbox() );
        createNewMessageButton.setOnAction(actionEvent -> showMailComposer() );
    }


    /**
     * This function updates the current shown mailbox. 
     * @param mailsFolderType the new value of the shown mailbox. it is of type Constants.COMMAND_FETCH_XXX so that once we execute the function to update the mailbox from the server, we already know the mailbox to fetch
     */
    private void updateCurrentMailFolder(int mailsFolderType) {
        appModel.setCurrentMailBoxFolder(mailsFolderType);
        refreshMailbox();
    }
    
    /**
     * this function is used to toggle the text value between log in and log out int he logout button
     */
    private void toggleLoginLogOutButton(){
        if(appModel.getClientIsLogged().getValue())
            logOutButton.setText("Log out");
        else
            logOutButton.setText("Log in");
    }

    /**
     * This function is used to show the mail viewver. it creates a new stage,
     * and shows the mail inside it.
     */
    private void showMailInfo() {
        FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("MailViewer.fxml"));
        try {
            Mail itemSelected = listViewMessages.getSelectionModel().getSelectedItem();
            Stage stage = new Stage();

            stage.setScene(loader.load());
            stage.setTitle(itemSelected.getSubject() + " - Muppets Mail Client");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(ClientApp.class.getResourceAsStream("ClientIcon.png"))));

            MailViewerController mailViewerController = loader.getController();
            
            mailViewerController.setClientModel(appModel);
            mailViewerController.setMail(itemSelected);
            mailViewerController.showMailFrom.setText(itemSelected.getFrom());
            mailViewerController.showMailTo.setText(itemSelected.getTo().toString());
            mailViewerController.showMailSubject.setText(itemSelected.getSubject());
            mailViewerController.messageBodyDisplay.setText(itemSelected.getMessage());

            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function shows the interface to create a new email
     *  */
    private void showMailComposer(){
        FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("MailComposer.fxml"));
        try {
            Stage stage = new Stage();
            stage.setScene(loader.load());
            stage.setTitle("Write new email - Muppets Mail Client");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(ClientApp.class.getResourceAsStream("ClientIcon.png"))));
            MailComposerController mailComposerController = loader.getController();
            mailComposerController.setClientModel(appModel);

            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
