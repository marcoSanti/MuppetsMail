package org.prog3.project.muppetsmail.Server.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.util.ArrayList;

public class ServerModel {

    private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    private ArrayList<MailBox> serverMailBoxes;


    public ServerModel(){
        this.serverMailBoxes = new ArrayList<>();
    }


    public void addMailBox(MailBox m){
        serverMailBoxes.add(m);
    }



    public ObservableList<LogEntry> getLogEntries(){return this.logEntries; }

    public synchronized void addLog(String message){
        logEntries.add(new LogEntry(message));
    }

}
