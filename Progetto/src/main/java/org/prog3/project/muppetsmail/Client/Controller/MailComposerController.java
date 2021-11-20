package org.prog3.project.muppetsmail.Client.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;

public class MailComposerController {

    /*
    * UI Vars
    *  */
    public TextField mailSubject;
    public TextField mailToRecipients;
    public Button mailSendButton;
    public Button mailSaveDraftButton;
    public Button mailDeleteDraftButton;
    public TextArea mailBodyViewer;

    private ClientModel appModel;

    public void setClientModel(ClientModel clientModel){
        this.appModel = clientModel;
    }

    public void setAppModel(ClientModel clientModel){ this.appModel = clientModel; }
}
