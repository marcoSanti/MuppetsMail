package org.prog3.project.muppetsmail.SharedModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.prog3.project.muppetsmail.Server.Model.Constants;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailBox implements Serializable {
    private ArrayList<Mail> inbox, sent,  deleted; //vars used to store indefinitely informations as well as when they are transported between hosts
    private String username;
    private transient ObservableList<Mail> sentObs, deletedObs, inboxObs; //variable used to store elements while app is running

    //this must be transient as it is not serializable and must be allocated by every client each time that it requires to
    //to enable, once the class is read, to set a writer, the method setObjectWriter is created
    private transient ObjectOutputStream writer;
    private transient String fileWriterPath = null;
    /*
    * At the beginning, when the mailbox is created, only the user id is required
    * */
    public MailBox(String username) {
        this.writer = writer;
        this.username = username;
        this.inbox = new ArrayList<>();
        this.sent = new ArrayList<>();
        this.deleted =new ArrayList<>();

    }


    /*
    * This method allows to crerate a file output stream,
    * to allow the class to be savedto local disk.
    * It requires the parameter fileName wich is the current fileName (with absolute / relative path)
    * */
    public synchronized void createOutputObjectWriter(String fileName) throws IOException {
        this.fileWriterPath = fileName;
    }


    /**
     * @param mailToAdd
     * @param mailBoxType can be 1: inbox, 2: sent, 3: deleted
     */
    public synchronized void addMail(Mail mailToAdd, int mailBoxType){

        switch (mailBoxType) {
            case Constants.MAILBOX_INBOX_FOLDER:
                inbox.add(mailToAdd);
                if(inboxObs != null) inboxObs.add(mailToAdd);
                break;
            case Constants.MAILBOX_SENT_FOLDER:
                sent.add(mailToAdd);
                if(sentObs != null)sentObs.add(mailToAdd);
                break;
            case Constants.MAILBOX_DELETED_FOLDER:
                deleted.add(mailToAdd);
                if(deletedObs != null)deletedObs.add(mailToAdd);
                break;
            default:
                System.out.println("Mailbox type not implemented!");
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
    * from and to are as follows:
    *   1 -> inbox
    *   2 -> sent
    *   3 -> deleted
    * if a message is deleted from deleted, then it is totally deleted!
    * the operation has to be done twice: once for the local running observable list, and once for the long term storage ArrayList
    */
    public synchronized void moveTo(Mail msg, int from, int dest ){
        ArrayList<Mail> fromArr=null;
        ArrayList<Mail> toArr=null;
        ObservableList<Mail> fromArrObs=null;
        ObservableList<Mail> toArrObs=null;

        System.out.println("FROM: " +from);
        System.out.println("TO: " +dest);

        switch (from){
            case Constants.MAILBOX_INBOX_FOLDER:
                System.out.println(inbox);
                inbox.remove(msg);
                System.out.println(inbox);
                if(inboxObs != null) inboxObs.remove(msg);
//                fromArr = inbox;
//                if(inboxObs != null)fromArrObs = inboxObs;
                break;
            case Constants.MAILBOX_SENT_FOLDER:
                sent.remove(msg);
                if(sentObs != null) sentObs.remove(msg);

//                fromArr = sent;
//                if(sentObs != null)fromArrObs = sentObs;
                break;
            case Constants.MAILBOX_DELETED_FOLDER:
                deleted.remove(msg);
                if(deletedObs != null) deletedObs.remove(msg);

//                fromArr = deleted;
//                if(deletedObs != null)fromArrObs = deletedObs;
                break;
            default:
                return;
        }


        switch (dest){
            case Constants.MAILBOX_INBOX_FOLDER:
                inbox.add(msg);
                if(inboxObs != null) inboxObs.remove(msg);

//                toArr = inbox;
//                if(inboxObs != null)toArrObs = inboxObs;
                break;
            case Constants.MAILBOX_SENT_FOLDER:
                sent.add(msg);
                if(sentObs != null) sentObs.remove(msg);

//                toArr = sent;
//                if(sentObs != null)toArrObs = sentObs;
                break;
            case Constants.MAILBOX_DELETED_FOLDER:
                deleted.add(msg);
                if(deletedObs != null) deletedObs.remove(msg);

//                toArr = deleted;
//                if(deletedObs != null)toArrObs = deletedObs;
                break;
            default:
                return;
        }
        System.out.println("FINISHED HERE");
    }


    /*this method generates the observaleList that will be shown into the
    listview.
    * */
    public void generateObservableItems(){
        inboxObs = FXCollections.observableArrayList();
        for(Mail m : inbox) inboxObs.add(m);

        sentObs = FXCollections.observableArrayList();
        for(Mail m : sent) sentObs.add(m);

        deletedObs = FXCollections.observableArrayList();
        for(Mail m : deleted) deletedObs.add(m);
    }

    /*
    * This method allows to delete messages older than days
    * */
    public synchronized void removeOldXmessages(int days){
        ArrayList<Mail> tmpList = new ArrayList<>();
        Date today = new Date();

        synchronized (deleted) {
            for (Mail m : deleted) {
                if (
                        Duration.ofDays(Math.abs(m.getDate().getTime() - today.getTime())).toDays() > days
                ) {
                    tmpList.add(m);
                }
            }
            for (Mail m : tmpList) {
                deleted.remove(m);
            }
        }
    }


    //This method allows a mailbox to be saved into hard disk
    public synchronized void saveToDisk() throws IOException {
        if(writer == null) writer = new ObjectOutputStream(new FileOutputStream(fileWriterPath));
        writer.writeObject(this);
    }


    /*
    * This method allows to empty the trash folder
    * */
    public synchronized void emptyAllTrash(){ this.deleted.clear(); }

    public String getUsername() {
        return username;
    }

    /*
    * Remember: every time we will access a single email from a folder, that operation will have
    * to be synchronized on the mail object itself
    * */

    public ObservableList<Mail> getInbox() {
        return inboxObs;
    }

    public ObservableList<Mail> getSent() {
        return sentObs;
    }

    public ObservableList<Mail> getDeleted() {
        return deletedObs;
    }
}

