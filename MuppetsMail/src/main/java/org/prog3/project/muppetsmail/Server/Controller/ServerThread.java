package org.prog3.project.muppetsmail.Server.Controller;

import javafx.application.Platform;
import org.prog3.project.muppetsmail.Server.Model.Constants;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNameDuplicated;
import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
    private Socket socket;
    ObjectInputStream serverInputStream = null;
    ObjectOutputStream serverOutputStream = null;
    ServerModel serverModel;

    public ServerThread(Socket socket, ServerModel model) {
        this.socket = socket;
        this.initialiseStreams();
        this.serverModel = model;
    }

    private void initialiseStreams() {
        try {
            serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
            serverInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {

        try {
            Object input = serverInputStream.readObject();

            if (input.getClass() == String.class) {
                String username = (String) input;
                this.addLogToGUI("Received request for username: " + username);

                if(serverModel.getMailBox(username) == null){ //if no mailbox exists for username, then mailbox is generated!
                    this.addLogToGUI("Mailbox " + username + " was not found! Creating a new mailbox!");
                    MailBox tmp = new MailBox(username);
                    tmp.generateObservableItems();
                    tmp.createOutputObjectWriter("./ServerMailBoxes/" + username + ".muppetsmail");
                    ArrayList<String> to = new ArrayList<>(); to.add(username + "@muppetsmail.org");
                    Mail welcomeMail = new Mail("welcomeMail", "welcome@muppetsmail.org", to , "Welcome to muppetsmail! an email Server and client by Marco Santimaria and Nicolò Vanzo! We hope you will enjoy our creation :-)", "Welcome to muppets mail!", Constants.MAILBOX_INBOX_FOLDER);
                    tmp.addMail(welcomeMail, Constants.MAILBOX_INBOX_FOLDER);

                    tmp.saveToDisk();
                    serverModel.addMailBox(tmp); //todo: return an error class to client in case a duplicated exists eve though it should never be fired!

                }
                serverOutputStream.writeObject(this.serverModel.getMailBox(username));

            } else if (input.getClass() == Mail.class) {
                Mail inputMail = (Mail) input;
                this.addLogToGUI("An sent email was generated from: " + inputMail.getFrom());
                serverModel.getMailBox(inputMail.getFrom()).addMail(inputMail, Constants.MAILBOX_SENT_FOLDER);

                boolean errorInSendingEmail = false;
                ArrayList<String> returnEmailError = new ArrayList<String>();
                returnEmailError.add(inputMail.getFrom());
                ArrayList<String> destUserNames = inputMail.getTo();

                for(String user: destUserNames) {
                    System.out.println("Trying to send an email to: " + user);
                    if(this.checkIfUsernameExist(user)) {
                        serverModel.getMailBox(user).addMail(inputMail,Constants.MAILBOX_INBOX_FOLDER);
                    } else {
                        errorInSendingEmail = true;
                        Mail errorEmail = new Mail("", "webmaster@muppetsmail.org", returnEmailError, "We were unable to deliver the email to " + user + "; with email subkect of: "
                                + inputMail.getSubject() + "Because user does not exist on our server!", "Unable to deliver email!", Constants.MAILBOX_INBOX_FOLDER );
                        serverModel.getMailBox(user).addMail(errorEmail, Constants.MAILBOX_INBOX_FOLDER);
                    }
                }

                if(errorInSendingEmail){
                    //todo: send notification to client that new inbox email are found!
                }

            } else {
                this.addLogToGUI("Received invalid command", "received invalid input of class:" + input.getClass() + " , from remote address: " + socket.getInetAddress());
            }


        } catch (IOException | ClassNotFoundException | MailBoxNameDuplicated | MailBoxNotFoundException  e) {
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

    private boolean checkIfUsernameExist(String user) {
        boolean userFound = false;
        if(serverModel.getMailBox(user)!=null) {
            userFound = true;
        }

        return userFound;
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
