package org.prog3.project.muppetsmail.Server.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNameDuplicated;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.util.ArrayList;

/**
 * This class is the server model
 */
public class ServerModel {

    private ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();
    private ArrayList<MailBox> serverMailBoxes;

    public ServerModel(){
        this.serverMailBoxes = new ArrayList<>();
    }

    /**
     * This function adds a mailbox loaded from the os disk to the loaded mailbox arrayList
     * @param mailBox the mailbox to be added
     * @throws MailBoxNameDuplicated
     */
    public void addMailBox(MailBox mailBox) throws MailBoxNameDuplicated {
        if(isUsernameAlreadyTaken(mailBox)) 
            throw new MailBoxNameDuplicated("A mail with the same username already exists, please check the username and retry");
        
        serverMailBoxes.add(mailBox);
    }

    /**
     * This functoin tells whether a mailbox with the same usernema has already been loaded into memory
     * @param mailBox the mailbox to be loaded
     * @return
     */
    private boolean isUsernameAlreadyTaken(MailBox mailBox) {
        boolean usernameDuplicated = false;
        for (int i = 0; i < serverMailBoxes.size() && !usernameDuplicated; i++) 
            if(serverMailBoxes.get(i).getUsername().equals(mailBox.getUsername())) 
                usernameDuplicated = true;
    
        return usernameDuplicated;
    }

    /**
     * this function return a mailbox
     * @param username the username of which the mailbox is required
     * @return the username mailbox
     */
    public MailBox getMailBox(String username) {
        MailBox mailBoxToReturn = null;
    
        for(int i = 0; i < this.serverMailBoxes.size() && (mailBoxToReturn == null); i++) 
            if(serverMailBoxes.get(i).getUsername().equals(username)) 
                mailBoxToReturn = serverMailBoxes.get(i);
                
            
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
