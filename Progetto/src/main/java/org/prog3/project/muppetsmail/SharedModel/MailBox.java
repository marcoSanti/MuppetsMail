package org.prog3.project.muppetsmail.SharedModel;

import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailNotFoundException;

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

    /*
    * At the beginning, when the mailbox is created, only the user id is required
    * */
    public MailBox(String username) {
        /*
        * TODO: creare sistema per evitare 2 mailbox stesso nome
        * */
        this.username = username;
        this.inbox = new ArrayList<>();
        this.sent = new ArrayList<>();
        this.deleted = new ArrayList<>();
    }

    /*
    * This methods return a mail message from a certain string id
    * */
    public Mail getEmail(String mailId, List<Mail> folder) throws MailNotFoundException{
        for(Mail t: folder) if(t.getMailId().equals(mailId)) return t;

        throw new MailNotFoundException("Mail with id " + mailId + " not found in given folder!!");
    }


    /*
    *This method allows to remove a message from a folder
    * and to move it to another folder
    */
    public void moveTo(Mail msg, List<Mail> from, List<Mail> to){
        from.remove(msg);
        to.add(msg);
    }

    /*
    * This method allows to delete messages older than days
    * */

    public void removeOldXmessages(int days){
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

    //TODO: aggiungere metodo per sync roba in jvm a file salvati -> nel controller!

    /*
    * This method allows to empty the trash folder
    * */
    public void emptyAllTrash(){ this.deleted.clear(); }

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

