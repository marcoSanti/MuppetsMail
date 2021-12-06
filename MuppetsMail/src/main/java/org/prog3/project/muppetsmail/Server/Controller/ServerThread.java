package org.prog3.project.muppetsmail.Server.Controller;

import javafx.application.Platform;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNameDuplicated;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;
import org.prog3.project.muppetsmail.SharedModel.MailWrapper;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
    private Socket socket;
    ServerModel serverModel;
    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;

    public ServerThread(Socket socket, ServerModel model) {
        this.socket = socket;
        this.serverModel = model;
    }


    @Override
    public void run() {
        try {
           createStreams();
           MailWrapper input = (MailWrapper) serverInputStream.readObject();
                        
            switch (input.getType()) {
                case Constants.COMMAND_FETCH_INBOX:
                    checkMailBoxExists(input.getUsername());
                    serverOutputStream.writeObject(new MailWrapper(serverModel.getMailBox(input.getUsername()).getInbox()));
                    break;
                case Constants.COMMAND_FETCH_DELETE:
                    serverOutputStream.writeObject(new MailWrapper(serverModel.getMailBox(input.getUsername()).getDeleted()));
                    break;
                case Constants.COMMAND_FETCH_SENT:
                    serverOutputStream.writeObject(new MailWrapper(serverModel.getMailBox(input.getUsername()).getSent()));
                    break;
                case Constants.COMMAND_SEND_MAIL:
                    try{
                        sendMail(input.getMailToSend(), input.getUsername());
                    }catch(IOException e){
                        addLogToGUI("Unable to save mailbox to disk", e.getStackTrace().toString());
                    }
                    break;
                    
                case Constants.COMMAND_DELETE_MAIL:
                
                    String username = input.getUsername();
                    MailBox mailBox = serverModel.getMailBox(username);
                    int mailBoxFolderTmp = input.getMailToSend().getCurrentMailBox();
                    
                    mailBox.moveTo(input.getMailToSend(), mailBoxFolderTmp, Constants.MAILBOX_DELETED_FOLDER);
                    input.getMailToSend().setCurrentMailBox(Constants.MAILBOX_DELETED_FOLDER);
                    
                    break;
                default:
                    break;
            }
        } catch (IOException | ClassNotFoundException  e) {
            this.addLogToGUI("Error in ServerThread", e.getMessage() + " of class: " + e.getClass());
            e.printStackTrace();

        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                this.addLogToGUI("Error in ServerThread on closing socket", e.getMessage());
            }
        }
    }

    private  void sendMail(Mail email, String senderUsername) throws IOException {
        for(String user : email.getTo()){
            MailBox tmp = serverModel.getMailBox(user);
            if(tmp!=null){
                tmp.addMail(email.clone(), Constants.MAILBOX_INBOX_FOLDER);
                serverModel.getMailBox(user).saveToDisk();
            } 
            else{
                ArrayList<String> errorListFrom = new ArrayList<>();
                errorListFrom.add(senderUsername);
                Mail errorMail = new Mail( "kermit@muppetsmail.com", errorListFrom, "I was unable to deliver message to " + user + " because this user does not exists!", "Unable to deliver message!", Constants.MAILBOX_INBOX_FOLDER);
                serverModel.getMailBox(senderUsername).addMail(errorMail, Constants.MAILBOX_INBOX_FOLDER);
            }
        }
        serverModel.getMailBox(senderUsername).addMail(email, Constants.MAILBOX_SENT_FOLDER);
        serverModel.getMailBox(senderUsername).saveToDisk();
    }

    private void checkMailBoxExists(String username){
        try{
            if(serverModel.getMailBox(username) == null){ //if no mailbox exists for username, then mailbox is generated!
                this.addLogToGUI("Mailbox " + username + " was not found! Creating a new mailbox!");
                MailBox tmp = new MailBox(username);
                tmp.createOutputObjectWriter("./ServerMailBoxes/" + username + ".muppetsmail");
                ArrayList<String> to = new ArrayList<>(); to.add(username + "@muppetsmail.org");
                Mail welcomeMail = new Mail( "welcome@muppetsmail.org", to , "Welcome to muppetsmail! an email Server and client by Marco Santimaria and Nicolò Vanzo! We hope you will enjoy our creation :-)", "Welcome to muppets mail!", Constants.MAILBOX_INBOX_FOLDER);
                tmp.addMail(welcomeMail, Constants.MAILBOX_INBOX_FOLDER);
                tmp.saveToDisk();
                serverModel.addMailBox(tmp); //todo: return an error class to client in case a duplicated exists eve though it should never be fired!
            }
        }catch(IOException | MailBoxNameDuplicated e){
            e.printStackTrace();
        }
    }

    private void createStreams() throws IOException {
        serverInputStream = new ObjectInputStream(socket.getInputStream());
        serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    private void addLogToGUI(String message){
        this.addLogToGUI(message, "");
    }

    private void addLogToGUI(String message, String detailed){
        Platform.runLater(new Runnable() { //done because a new task is require to update model and gui
            @Override
            public void run() {
                serverModel.addLog(message, detailed);
            }
        });
    }

    public Socket getSocket() {
        return socket;
    }
}
