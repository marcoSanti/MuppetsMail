package org.prog3.project.muppetsmail.SharedModel;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the message format that is used to communicate between client and server.
 * The client forms a message with this class, sends it to the server.
 * The server accepts the message and now it can reads it.
 * We didn't use a simple Mail class because we need to send more informations than the mail itself
 */
public class MailWrapper implements Serializable{
    private ArrayList<Mail> listToWrap;
    private Mail mailToSend;
    private String username;
    private Integer commandReceivedFromClient; //command that is sent

    public MailWrapper(ArrayList<Mail> listToWrap, Mail mailToSend, Integer commandReceivedFromClient, String username) {
        this.username = username;
        this.listToWrap = listToWrap;
        this.mailToSend = mailToSend;
        this.commandReceivedFromClient = commandReceivedFromClient;
    }

    public MailWrapper(int commandReceivedFromClient, String username) {
        this(null, null, commandReceivedFromClient, username);
    }

    public MailWrapper(ArrayList<Mail> mailsFolder){
        this(mailsFolder, null, null, null);
    }

    public MailWrapper(Mail m, int commandReceivedFromClient, String username) {
        this(null, m, commandReceivedFromClient, username);
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
        return commandReceivedFromClient;
    }
}