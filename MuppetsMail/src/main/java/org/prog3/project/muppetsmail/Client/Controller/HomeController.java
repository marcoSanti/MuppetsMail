package org.prog3.project.muppetsmail.Client.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.prog3.project.muppetsmail.Client.ClientApp;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.Client.ViewObjs.CellFactory;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    /*
    * UI Vars
    * */

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

    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
        usernameLabel.textProperty().bind(appModel.getUsername());
        serverLabel.textProperty().bind(appModel.getEndpoint());

        clientModel.getClientIsLogged().addListener(
                (observableValue, aBoolean, t1) -> toggleLoginLogOutButton()
        );

        /*
        * The onAction for email buttons are defined here as it is
        * required the model to not be null.
        * */
        appModel.getClientIsLogged().addListener(
                (observableValue, oldVal, newVal) -> {
                            if((appModel != null) && newVal){

                                setHomeElements();

                            }else { //if disconnected or not yet connected
                                listViewMessages.setItems(null);
                                sentButton.setOnAction(null);
                                trashButton.setOnAction(null);
                                inboxButton.setOnAction(null);
                                createNewMessageButton.setOnAction(null);
                                connectionStatusCircle.setFill(Color.RED);
                            }
                }
        );

        refreshMailbox.setOnAction(actionEvent -> {
            this.refreshMailbox();
        });
    }

    private void setHomeElements() {
        connectionStatusCircle.setFill(Color.LAWNGREEN);
        listViewMessages.setItems(appModel.getCurrentMailFolder());

        listViewMessages.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount()==2) {
                    showMailInfo();
                }
            }
        });

        inboxButton.setOnAction(actionEvent -> {
            updateCurrentMailFolder(Constants.COMMAND_FETCH_INBOX);
        });
        trashButton.setOnAction(actionEvent -> {
            updateCurrentMailFolder(Constants.COMMAND_FETCH_DELETE);
            
        });
        sentButton.setOnAction(actionEvent -> {
            updateCurrentMailFolder(Constants.COMMAND_FETCH_SENT);
        });

        createNewMessageButton.setOnAction(actionEvent -> showMailComposer() );
    }

    private void updateCurrentMailFolder(int mailsFolderType) {
        Object lock = new Object();
        appModel.connectionManager.runTask(mailsFolderType, lock);
        try {
            synchronized (lock) {
                lock.wait();
            }
            listViewMessages.setItems(appModel.getCurrentMailFolder());
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void refreshMailbox() {
        MailBox tmp;
        Object lock = new Object();
        //TODO: change the command because we're refreshing the inbox in this way
        appModel.connectionManager.runTask(Constants.COMMAND_FETCH_INBOX, lock);
        try {
            synchronized (lock){
                lock.wait();
            }
            //TODO: riscaricare la mailbox
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setLoginStage (Stage lStage){this.loginStage = lStage;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        connectionStatusCircle.setFill(Color.RED);

        listViewMessages.setCellFactory(mailListView -> new CellFactory(appModel));

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

    private void showMailInfo() {
        FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("MailViewer.fxml"));
        try {
            Stage stage = new Stage();
            stage.setScene(loader.load());
            stage.setTitle("Mail info - Muppets Mail Client");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(ClientApp.class.getResourceAsStream("ClientIcon.png"))));

            MailViewerController mailViewerController = loader.getController();
            Mail itemSelected = listViewMessages.getSelectionModel().getSelectedItem();
            mailViewerController.setClientModel(appModel);
            mailViewerController.setMail(itemSelected);

            mailViewerController.showMailFrom.setText(itemSelected.getFrom());
            mailViewerController.showMailTo.setText(itemSelected.getTo().toString());
            mailViewerController.showMailSubject.setText(itemSelected.getSubject());
            mailViewerController.messageBodyDisplay.setText(itemSelected.getMessage());

            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

            MailComposerController composerController = loader.getController();
            composerController.setClientModel(appModel);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
