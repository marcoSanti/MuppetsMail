package org.prog3.project.muppetsmail.Server.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import org.prog3.project.muppetsmail.Server.Model.LogEntry;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    /*
    * UI Vars
    * */

    public Button startServerButton;
    public Button stopServerButton;
    public Button restartServerButton;
    public Button serverConfigButton;
    public Button sendCommandButton;
    public TextField commandInput;
    public ListView<LogEntry> listView;
    /**
     * Others variables
     */
    List<File> mailbox;
    ServerModel model;
    private final String mailBoxPath = "./ServerMailBoxes/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startServerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.addLog("Starting server");
            }
        });


        stopServerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.addLog("Stopping server");
            }
        });

        restartServerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.addLog("Restarting server");
            }
        });


    }


    private void loadMailboxes() throws MailBoxNotFoundException {
    //if mailbox folder does not exists, it will be created
        try {
            Files.createDirectory(Paths.get(mailBoxPath));
            model.addLog("MailBox folder not found! creating a new one!");
        } catch (IOException e) {
            model.addLog("MailBox folder found! folder is: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        File mailBoxesdir = new File(mailBoxPath);
        if(mailBoxesdir.listFiles()!=null) {
            model.addLog("Loading mailboxes from folder");
            this.mailbox = Arrays.asList(mailBoxesdir.listFiles());
            for(File f: this.mailbox) {
                String logMsg = "Loading " + f.getName().substring(0, f.getName().indexOf(".muppetsmail")) + " mailbox...";
                model.addLog(logMsg);
                System.out.println(logMsg);
            }
            model.addLog("Finished loading mailboxes!");
        } else {
            model.addLog("LOG ERROR MESSAGE IN LOADMAILBOXES: mailBox dir not found!");
            throw new MailBoxNotFoundException("LOG ERROR MESSAGE IN LOADMAILBOXES: mailBox dir not found!");
        }
    }

    //this is our "initialize" since we cannot do anything until we set the application model!
    public void setModel(ServerModel model) {
        this.model = model;
        listView.setItems(model.getLogEntries());
        try {
            this.loadMailboxes();
        } catch (MailBoxNotFoundException e) {
            e.printStackTrace();
        }
    }
}
