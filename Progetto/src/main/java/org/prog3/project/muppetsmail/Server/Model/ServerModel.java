package org.prog3.project.muppetsmail.Server.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerModel {

    private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    public ObservableList<LogEntry> getLogEntries(){return this.logEntries; }

    public void addLog(String message){
        logEntries.add(new LogEntry(message));
    }

}
