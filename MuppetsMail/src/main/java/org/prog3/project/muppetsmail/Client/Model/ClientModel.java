package org.prog3.project.muppetsmail.Client.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.prog3.project.muppetsmail.Client.Controller.ConnectionManager;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is the model for the client app 
 */

public class ClientModel implements Serializable {
    private int currentMailBox;
    private StringProperty username;
    private StringProperty endpoint;
    private StringProperty endpointPort;
    private StringProperty connectionStatus;
    private ObservableList<Mail> currentMailFolder;
    private transient BooleanProperty clientIsLogged;
    public ConnectionManager connectionManager;
    

    public ClientModel() {
        this.username = new SimpleStringProperty();
        this.endpoint = new SimpleStringProperty();
        this.endpointPort = new SimpleStringProperty();
        this.connectionStatus = new SimpleStringProperty();
        this.clientIsLogged = new SimpleBooleanProperty(false);
        this.currentMailBox = Constants.COMMAND_FETCH_INBOX;
    }

    /**
     * This function converts an arraylist to an observable list. this is required as an observable list does not
     * implements Serializable, and thus it is not possible to send it through a socket
     * @param arrList the arraylist to be converted
     * @return the observable list associated with the arraylist
     */
    public static ObservableList<Mail> convertArrayListToObservableList(ArrayList<Mail> arrList){
        ObservableList<Mail> tmp = FXCollections.observableArrayList();
        tmp.addAll(arrList);
        Collections.reverse(tmp);
        return tmp;
    
    }

    /**
     * returns the current observable list
     * @return
     */
    public ObservableList<Mail> getCurrentMailFolder() {
        return this.currentMailFolder;
    }

    /**
     * sets the current mailbox the user is using
     * @param currentMailFolder the current mailbox. 
     */
    public void setCurrentMailFolder(ObservableList<Mail> currentMailFolder) {
        this.currentMailFolder = currentMailFolder;
    }

    /**
     * 
     * @return whether the client is logged or not
     */
    public BooleanProperty getClientIsLogged(){ return this.clientIsLogged; }

    /**
     * 
     * @return the endpoint string property
     */
    public StringProperty getEndpoint() {
        return this.endpoint;
    }

    /**
     * 
     * @return the endpoint port string property
     */
    public StringProperty getEndpointPort() {
        return this.endpointPort;
    }

    /**
     * 
     * @return the connection status string property
     */
    public StringProperty getConnectionStatus() {
        return this.connectionStatus;
    }

    /**
     * 
     * @return the username string property
     */
    public StringProperty getUsername() {
        return this.username;
    }


    /**
     * This function sets the username
     * @param username username
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * this function sets the current mail box folder.
     * @param mailboxName a value taken from the Constatns.COMMAND_FETCH_XXX
     */
    public void setCurrentMailBoxFolder(int mailboxName){ 
        this.currentMailBox = mailboxName; 
    }

    /**
     * @return the current mailbox folder , which also corresponds to the command used to fetch the current mailbox
     */
    public int getCurrentMailBoxFolder(){ 
        return this.currentMailBox; 
    }
}
