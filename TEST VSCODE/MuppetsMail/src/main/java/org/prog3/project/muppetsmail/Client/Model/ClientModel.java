package org.prog3.project.muppetsmail.Client.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.prog3.project.muppetsmail.Client.Controller.ConnectionManager;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.Serializable;

public class ClientModel implements Serializable {


    private StringProperty username;
    private StringProperty endpoint;
    private StringProperty endpointPort;
    private StringProperty connectionStatus;
    private MailBox userMailBox;
    private transient BooleanProperty clientIsLogged;
    public ConnectionManager connectionManager;


    public ClientModel() {
        this.username = new SimpleStringProperty();
        this.endpoint = new SimpleStringProperty();
        this.endpointPort = new SimpleStringProperty();
        this.connectionStatus = new SimpleStringProperty();
        this.clientIsLogged = new SimpleBooleanProperty(false);
    }

    public MailBox getUserMailBox() {
        return userMailBox;
    }

    public void setUserMailBox(MailBox userMailBox) {
        this.userMailBox = userMailBox;
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
}
