package org.prog3.project.muppetsmail.Server.Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.File;
import java.net.URL;
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
    public ListView listView;
    /**
     * Others variables
     */
    List<File> mailbox;
    ServerModel model;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.loadMailboxes();
//            this.writeInListView();
        } catch (MailBoxNotFoundException e) {
            e.printStackTrace();
        }
    }

//    private void writeInListView() {
//        this.listView.
//    }

    private void loadMailboxes() throws MailBoxNotFoundException {
        File mailBoxesdir = new File("./Mailboxes");
        if(mailBoxesdir.listFiles()!=null) {
            this.mailbox = Arrays.asList(mailBoxesdir.listFiles());
            for(File f: this.mailbox) {
                System.out.println("LOG MESSAGE: file name in mailboxes dir: " +f.getName());
            }
        } else {
            throw new MailBoxNotFoundException("LOG ERROR MESSAGE IN LOADMAILBOXES: mailBox dir not found!");
        }
    }

    public void setModel(ServerModel model) {
        this.model = model;
    }
}
