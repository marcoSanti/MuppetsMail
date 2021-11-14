package org.prog3.project.muppetsmail.SharedModel;

import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailNotFoundException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailBox implements Serializable {
    private ArrayList<Mail> inbox;
    private ArrayList<Mail> sent;
    private ArrayList<Mail> deleted;
    private String username;
    private transient final ObjectOutputStream writer;

    /*
    * At the beginning, when the mailbox is created, only the user id is required
    * */
    public MailBox(String username, ObjectOutputStream writer) {
        /*
        * TODO: creare sistema per evitare 2 mailbox stesso nome
        * */
        this.writer = writer;
        this.username = username;
        this.inbox = new ArrayList<>();
        this.sent = new ArrayList<>();
        this.deleted = new ArrayList<>();
    }

    /**
     * @param mailToAdd
     * @param mailBoxType can be 1: inbox, 2: deleted, 3: sent
     */
    public synchronized void addMail(Mail mailToAdd, int mailBoxType) throws MailBoxNotFoundException {
        switch (mailBoxType) {
            case 1:
                inbox.add(mailToAdd);
            case 2:
                deleted.add(mailToAdd);
            case 3:
                sent.add(mailToAdd);
            default:
                throw new MailBoxNotFoundException("LOG ERROR: MAILBOX WIHT ID: "+mailBoxType+" NOT VALID");
        }
    }

    /*
    * This methods return a mail message from a certain string id
    * */
    public synchronized Mail getEmail(String mailId, List<Mail> folder) throws MailNotFoundException{
        for(Mail t: folder) if(t.getMailId().equals(mailId)) return t;

        throw new MailNotFoundException("Mail with id " + mailId + " not found in given folder!!");
    }


    /*
    *This method allows to remove a message from a folder
    * and to move it to another folder
    */
    public synchronized void moveTo(Mail msg, List<Mail> from, List<Mail> to){
        from.remove(msg);
        to.add(msg);
    }

    /*
    * This method allows to delete messages older than days
    * */

    public synchronized void removeOldXmessages(int days){
        ArrayList<Mail> tmpList = new ArrayList<>();
        Date today = new Date();

        for(Mail m : deleted){ //TODO: test perchè non sono sicuro
            if(
                    Duration.ofDays(Math.abs(m.getDate().getTime() - today.getTime())).toDays() > days
            ){
                    tmpList.add(m);
            }
        }

        for(Mail m: tmpList){
            deleted.remove(m);
        }
    }

    //This method allows a mailbox to be saved into hard disk
    public void writeToDisk() throws IOException {
        synchronized (this){
            writer.writeObject(this);
        }
    }

    //TODO: aggiungere metodo per sync roba in jvm a file salvati -> nel controller!

    /*
    * This method allows to empty the trash folder
    * */
    public synchronized void emptyAllTrash(){ this.deleted.clear(); }

    public String getUsername() {
        return username;
    }

    public ArrayList<Mail> getInbox() {
        return inbox;
    }

    public ArrayList<Mail> getSent() {
        return sent;
    }

    public ArrayList<Mail> getDeleted() {
        return deleted;
    }
}

