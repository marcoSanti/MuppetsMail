package org.prog3.project.muppetsmail.Model;

import org.prog3.project.muppetsmail.Model.Exceptions.MailNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
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
        this.inbox = new ArrayList<Mail>();
        this.sent = new ArrayList<Mail>();
        this.deleted = new ArrayList<Mail>();
    }

    public Mail getEmail(String mailId, List<Mail> folder) throws MailNotFoundException{
        for(Mail t: folder) if(t.getMailId().equals(mailId)) return t;

        throw new MailNotFoundException("Mail with id " + mailId + " not found in given folder!!");
    }


    //TODO: Metodi per spostare mail da inbox / sent a deleted


    //TODO: aggiungere metodo per sync roba in jvm a file salvati -> nel controller!

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

