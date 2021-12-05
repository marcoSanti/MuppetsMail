package org.prog3.project.muppetsmail.SharedModel;
import java.io.Serializable;
import java.util.ArrayList;


public class MailWrapper implements Serializable{
    private ArrayList<Mail> listToWrap;
    private Mail mailToSend;
    private String username;
    private Integer type;

    public MailWrapper(ArrayList<Mail> listToWrap, Mail mailToSend, Integer type, String username) {
        this.username = username;
        this.listToWrap = listToWrap;
        this.mailToSend = mailToSend;
        this.type = type;
    }

    public MailWrapper(int type, String username) {
        this(null, null, type, username);
    }

    public MailWrapper(ArrayList<Mail> mailsFolder){
        this(mailsFolder, null, null, null);
    }

    public ArrayList<Mail> getMailsFolder() {
        return listToWrap;
    }  

    public Mail getMailToSend() {
        return mailToSend;
    }

    public String getUsername() {
        return username;
    }

    public int getType() {
        return type;
    }
}