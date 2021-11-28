package org.prog3.project.muppetsmail.Server.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNameDuplicated;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.util.ArrayList;

public class ServerModel {

    private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();
    private ArrayList<MailBox> serverMailBoxes;

    public ServerModel(){
        this.serverMailBoxes = new ArrayList<>();
    }

    public void addMailBox(MailBox mailBox) throws MailBoxNameDuplicated {
        if(isUsernameAlreadyTaken(mailBox)) {
            throw new MailBoxNameDuplicated("A mail with the same username already exists, please check the username and retry");
        } else {
            serverMailBoxes.add(mailBox);
        }
    }

    private boolean isUsernameAlreadyTaken(MailBox mailBox) {
        boolean usernameDuplicated = false;
        for (int i = 0; i < serverMailBoxes.size() && !usernameDuplicated; i++) {
            if(serverMailBoxes.get(i).getUsername().equals(mailBox.getUsername())) {
                usernameDuplicated = true;
            }
        }
        return usernameDuplicated;
    }


    public MailBox getMailBox(String username) {
        MailBox mailBoxToReturn = null;
        boolean mailFound = false;
        for(int i = 0; i < this.serverMailBoxes.size() && !mailFound; i++) {
            if(serverMailBoxes.get(i).getUsername().equals(username)) {
                mailBoxToReturn = serverMailBoxes.get(i);
                mailFound = true;
            }
        }
        return mailBoxToReturn;
    }
    
    public ObservableList<LogEntry> getLogEntries(){return this.logEntries; }

    public synchronized void addLog(String message){
        logEntries.add(new LogEntry(message));
        System.out.println(message);
    }

    public synchronized void addLog(String message, String detailedMessage){
        logEntries.add(new LogEntry(message, detailedMessage));
        System.out.println(message);
    }
}
