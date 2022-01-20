package org.prog3.project.muppetsmail.Server.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.prog3.project.muppetsmail.Server.Model.LogEntry;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNameDuplicated;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    /*
     * UI Vars
     * */

    public Button startServerButton;
    public Button stopServerButton;
    public Button restartServerButton;
    public Button serverClearButton;
    public ListView<LogEntry> listView;
    public TextField detailedTimestamp;
    public TextArea detailedMessage;
    public TextField message;
    /**
     * Others variables
     */
    ServerModel model;
    Thread serverThreadManager;
    private ServerThreadManager serverThreadManagerClass;

    private final String mailBoxPath = "./ServerMailBoxes/";

    /**
     * This function initializes the server front end by adding the handler to the button and 
     * populating the listview
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.buttonsSetup();
        this.listviewSetup();
    }

    /**
     * This function initializes the list view. in particular, it adds a change listener to the current selected 
     * item in the listView, so that the fields (on the bottom of the view) that shows the informations about the log message
     * are filled accordingly to the new selected item, or emptied if no item is selected.
     */
    private void listviewSetup() {
        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<LogEntry>() {
                    @Override
                    public void changed(ObservableValue<? extends LogEntry> observableValue, LogEntry oldValue, LogEntry newValue) {
                        if (newValue != null) {
                            detailedMessage.setText(newValue.getDetailedMessage());
                            detailedTimestamp.setText(newValue.getTimestamp().toString());
                            message.setText(newValue.getMessage());
                        } else {
                            detailedMessage.setText("");
                            detailedTimestamp.setText("");
                            message.setText("");
                        }
                    }
                }
        );
    }

    /**
     * This function adds the handlers to the button action event
     */
    private void buttonsSetup() {
        startServerButton.setOnAction(actionEvent -> startServer() );
        stopServerButton.setOnAction(actionEvent -> stopServer() );
        restartServerButton.setOnAction(actionEvent -> restartServer() );
        serverClearButton.setOnAction(actionEvent -> model.getLogEntries().clear() );
    }

    /**
     * This function loads the mailboxes from the os disk.
     * @throws MailBoxNotFoundException
     */
    private void loadMailboxes() throws MailBoxNotFoundException {
        ObjectInputStream mailBoxReader = null;
        //if mailbox folder does not exists, it will be created, otherwise an exception is thrown because the folder already exists.
        try {
            Files.createDirectory(Paths.get(mailBoxPath));
            model.addLog("MailBox folder not found! creating a new one!");
        } catch (IOException e) {
            model.addLog("MailBox folder found!", "Folder is: " + e.getMessage());
        }

        File[] files = new File(mailBoxPath).listFiles((dir, name) -> name.matches("*.muppetsmail")); //list all the mailboxes with a regex

        if (files != null) {
            model.addLog("Loading mailboxes from folder");

            for (File f : files) {

                //loading mailboxes into memory from file
              try {
                    mailBoxReader = new ObjectInputStream(new FileInputStream(f));
                    MailBox tmp = (MailBox) mailBoxReader.readObject();
                    tmp.createOutputObjectWriter(f.toString()); //create the output writer once the class has been loaded

                    model.addMailBox(tmp);
                    model.addLog("Mailbox loaded", "Loaded " + f.getName().substring(0, f.getName().indexOf(".muppetsmail")) + " mailbox, from file " + f.toString());
                } catch (IOException | ClassNotFoundException | MailBoxNameDuplicated e) {
                    model.addLog("Error loading mailbox!", "Mailbox: " + f + "\n" + e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
            model.addLog("Finished loading mailboxes!");
        } else {
            model.addLog("Error in loading mail boxes!", "Mailbox folder was not found!");
            throw new MailBoxNotFoundException("LOG ERROR MESSAGE IN LOADMAILBOXES: mailBox dir not found!");
        }
        this.startServer();
    }

    /**
     * this is our "initialize" since we cannot do anything until we set the application model!
     * @param model the server model
     */
    public void setModel(ServerModel model) {
        this.model = model;
        listView.setItems(model.getLogEntries());
        try {
            this.loadMailboxes();

        } catch (MailBoxNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * This function starts the server, which means the threadPool is initialized so that it is able to accept new networkTask to be run
     */
    private void startServer() {

       if((serverThreadManagerClass == null) || (serverThreadManagerClass!=null && !serverThreadManagerClass.isRunning())){
            serverThreadManagerClass = new ServerThreadManager(this.model);
            serverThreadManager = new Thread(serverThreadManagerClass);
            serverThreadManager.start();
            model.addLog("Server started");
        }else{
            model.addLog("Unable to start server.", "Server is already running.");
        }

    }

    /**
     * this function stops the server, by stopping the threadPool from accepting new threads
     */
    private void stopServer() {
       if(serverThreadManagerClass != null && serverThreadManagerClass.isRunning()) {
           serverThreadManagerClass.stopServer();
           model.addLog("Server stopped");
       }
       else model.addLog("Unable to stop server!", "Server was not started.");
    }

    /**
     * This function restarts the server
     */
    private void restartServer(){
        if(serverThreadManagerClass!=null && serverThreadManagerClass.isRunning()) {
            this.stopServer();
            this.startServer();

        } else {
            this.startServer();
        }
    }

}
