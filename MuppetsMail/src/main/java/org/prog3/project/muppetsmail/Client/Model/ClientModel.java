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

    public static ObservableList<Mail> convertArrayListToObservableList(ArrayList<Mail> arrList){
        ObservableList<Mail> tmp = FXCollections.observableArrayList();
        tmp.addAll(arrList);
        Collections.reverse(tmp);
        return tmp;
    
    }

    public ObservableList<Mail> getCurrentMailFolder() {
        return this.currentMailFolder;
    }

    public void setCurrentMailFolder(ObservableList<Mail> currentMailFolder) {
        this.currentMailFolder = currentMailFolder;
    }


    public BooleanProperty getClientIsLogged(){ return this.clientIsLogged; }

    public StringProperty getEndpoint() {
        return this.endpoint;
    }

    public StringProperty getEndpointPort() {
        return this.endpointPort;
    }

    public StringProperty getConnectionStatus() {
        return this.connectionStatus;
    }

    public StringProperty getUsername() {
        return this.username;
    }


    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setCurrentMailBoxFolder(int mailboxName){ this.currentMailBox = mailboxName; }
    public int getCurrentMailBoxFolder(){ return this.currentMailBox; }
}
