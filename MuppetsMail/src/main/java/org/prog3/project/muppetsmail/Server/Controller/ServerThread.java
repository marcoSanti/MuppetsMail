package org.prog3.project.muppetsmail.Server.Controller;

import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNameDuplicated;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;
import org.prog3.project.muppetsmail.SharedModel.MailWrapper;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server thread is the class that will be going to serve a request made by 
 * the client
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private ServerModel serverModel;
    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;

    /**
     * 
     * @param socket Socket n which the request has to be served
     * @param model The server model which contains all the informations
     */
    public ServerThread(Socket socket, ServerModel model) {
        this.socket = socket;
        this.serverModel = model;
    }

    
    @Override
    public void run() {
        try {
            createStreams();
            MailWrapper input = (MailWrapper) serverInputStream.readObject();

            this.handleRequestFromClient(input);
            
        } catch (EOFException E) {
            Utils.addLogToGUI("Socket closed by client", serverModel);

        } catch (IOException | ClassNotFoundException e) {
            Utils.addLogToGUI("Error in ServerThread", e.getMessage() + " of class: " + e.getClass(), serverModel);
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                Utils.addLogToGUI("Error in ServerThread on closing socket", e.getMessage(), serverModel);
            }
        }
    }

    /**
     * This function checks for new emails in server, so that a count of how many 
     * new messages can be returned to the client.
     * @param input the data recived from the socket
     */
    private void checkForNewMail(MailWrapper input){
        String username = input.getUsername();
        ArrayList<Mail> mbTmp = serverModel.getMailBox(username).getInbox();
        Integer count = 0;
        
        for(Mail m : mbTmp){
            if(m.isMailNew()){
                count++;
                m.setMailAsRead();
            } 
        }
        
        try{ 
            serverOutputStream.writeObject(count);
        }catch(IOException e){
            Utils.addLogToGUI("Connection closed by client", e.getMessage(), serverModel);
        }
    }

    /**
     * This function is resposable to invoke the correct method wich handles the request from the client
     * @param input the input from the client wich contains the request and other helpful data
     * @throws IOException
     */
    private void handleRequestFromClient(MailWrapper input) throws IOException {
        switch (input.getType()) {
            case Constants.COMMAND_FETCH_INBOX:
                checkMailBoxExists(input.getUsername());
                serverOutputStream.writeObject(new MailWrapper(serverModel.getMailBox(input.getUsername()).getInbox()));
                break;

            case Constants.COMMAND_FETCH_DELETE:
                serverOutputStream.writeObject(new MailWrapper(serverModel.getMailBox(input.getUsername()).getDeleted()));
                Utils.addLogToGUI("Fetched deleted mails for user: ", input.getUsername(), serverModel);
                break;

            case Constants.COMMAND_FETCH_SENT:
                serverOutputStream.writeObject(new MailWrapper(serverModel.getMailBox(input.getUsername()).getSent()));
                Utils.addLogToGUI("Fetched sent mails for user: ", input.getUsername(), serverModel);
                break;

            case Constants.COMMAND_SEND_MAIL:
                try {
                    sendMail(input.getMailToSend(), input.getUsername());
                    Utils.addLogToGUI("Mail sent from: " + input.getMailToSend().getFrom() + " to: " + input.getMailToSend().getTo(), "Mail sent", serverModel);
                } catch (IOException e) {
                    Utils.addLogToGUI("Unable to save mailbox to disk", e.getStackTrace().toString(), serverModel);
                }
                break;

            case Constants.COMMAND_DELETE_MAIL:
                deleteMail(input);
                Utils.addLogToGUI("Mail with id: " + input.getMailToSend().getMailId() + "", "Mail sent", serverModel);
                break;

            case Constants.COMMAND_CHECK_NEW_MAIL_PRESENCE :
                checkForNewMail(input);
            break;
            default:
                break;
        }
    }

    /**
     * This function is responsable to delete an email
     * @param input the data from the client 
     */
    private void deleteMail(MailWrapper input) {
        String username = input.getUsername();
        MailBox mailBox = serverModel.getMailBox(username);
        int currentMailBox = input.getMailToSend().getCurrentMailBox();

        mailBox.moveTo(input.getMailToSend(), currentMailBox, Constants.MAILBOX_DELETED_FOLDER);
        try {
            mailBox.saveToDisk();
            Utils.addLogToGUI("Mailbox saved to disk!", serverModel);
        } catch (IOException e) {
            Utils.addLogToGUI("Unable to save mailBox to disk!", e.getStackTrace().toString(), serverModel);
        }
    }

    /**
     * This function is responsable to send an email. it recives an email from a client, and then forwards a copy of the message to 
     * each of the recipients of the email, by cloning the email. This is done because otherwise, if evry recipients share the same message (as reference),
     * if one of them deletes the message, it will be deleted for everyone.
     * This function also reply to the sendere with an email, for evry recipient which cannot be found
     * @param email the email to be sent
     * @param senderUsername the user which has sent the email
     * @throws IOException
     */
    private void sendMail(Mail email, String senderUsername) throws IOException {
        for (String user : email.getTo()) {
            if (user != "") {
                MailBox tmp = serverModel.getMailBox(user);
                if (tmp != null) {
                    tmp.addMail(email.clone(), Constants.MAILBOX_INBOX_FOLDER);
                    serverModel.getMailBox(user).saveToDisk();
                } else {
                    ArrayList<String> errorListFrom = new ArrayList<>();
                    errorListFrom.add(senderUsername);
                    Mail errorMail = new Mail("Kermit", errorListFrom,
                            "I was unable to deliver message to " + user + " because this user does not exists!",
                            "Unable to deliver message!", Constants.MAILBOX_INBOX_FOLDER);
                    serverModel.getMailBox(senderUsername).addMail(errorMail, Constants.MAILBOX_INBOX_FOLDER);
                }
            }
        }
        serverModel.getMailBox(senderUsername).addMail(email, Constants.MAILBOX_SENT_FOLDER);
        serverModel.getMailBox(senderUsername).saveToDisk();
    }

    /**
     * This function checks for a mailbox, and if the mailbox does not exists, it creates a new one. 
     * @param username the username to check for a mailbox existance
     */
    private void checkMailBoxExists(String username) {
        try {
            if (serverModel.getMailBox(username) == null) { // if no mailbox exists for username, then mailbox is
                                                            // generated!
                Utils.addLogToGUI("Mailbox " + username + " was not found! Creating a new mailbox!", serverModel);
                MailBox tmp = new MailBox(username);
                tmp.createOutputObjectWriter("./ServerMailBoxes/" + username + ".muppetsmail");
                ArrayList<String> to = new ArrayList<>();
                to.add(username);
                Mail welcomeMail = new Mail("Welcome", to,
                        "Welcome to muppetsmail! an email Server and client by Marco Santimaria and Nicolò Vanzo! We hope you will enjoy our creation :-)",
                        "Welcome to muppets mail!", Constants.MAILBOX_INBOX_FOLDER);
                tmp.addMail(welcomeMail, Constants.MAILBOX_INBOX_FOLDER);
                tmp.saveToDisk();
                serverModel.addMailBox(tmp); 
            }
        } catch (IOException | MailBoxNameDuplicated e) {
            e.printStackTrace();
        }
    }

    /**
     * This function creates the input and ouput streams from the socket
     * @throws IOException
     */
    private void createStreams() throws IOException {
        serverInputStream = new ObjectInputStream(socket.getInputStream());
        serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    
}
