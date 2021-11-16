package org.prog3.project.muppetsmail.Client.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;

public class MailViewerController {
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

    ClientModel appModel;

    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
    }
}
