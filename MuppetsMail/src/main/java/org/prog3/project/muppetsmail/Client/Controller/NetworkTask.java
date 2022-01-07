package org.prog3.project.muppetsmail.Client.Controller;

import org.prog3.project.muppetsmail.SharedModel.Utils;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailWrapper;

import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is a thread that is executed whenever 
 * a server request is done by the client
 */

public class NetworkTask implements Runnable {
    ObjectOutputStream clientOutputStream;
    ObjectInputStream clientInputStream;
    Socket socket = null;
    ClientModel appModel;
    final Object lock;
    int command;
    Mail mail;

    /**
     * 
     * @param appModel the application model
     * @param command the command to be executed
     * @param lock the lock that will be used to signal the task is completed and data is available
     */
    public NetworkTask(ClientModel appModel, int command, Object lock) {
        this.appModel = appModel;
        this.initialiseSocket(appModel.getEndpoint().getValue(), appModel.getEndpointPort().getValue());
        this.initialiseStreams();
        this.command = command;
        this.lock = lock;
    }

    /**
     * 
     * @param appModel the application model
     * @param command the command to be executed
     * @param lock the lock that will be used to signal the task is completed and data is available
     * @param mail The email object to be sent to the server
     */
    public NetworkTask(ClientModel appModel, int command, Object lock, Mail mail) {
        this(appModel, command, lock);
        this.mail = mail;
    }

    /**
     * This method checks wich command is requested, and run the code accordingly.
     * Once the job is done, it signals the parent task that the job is done trough the lock object
     */
    @Override
    public void run() {

        try {
            switch (command) {
                case Constants.COMMAND_FETCH_INBOX:
                case Constants.COMMAND_FETCH_DELETE:
                case Constants.COMMAND_FETCH_SENT:
                    synchronized (lock) {
                        this.fetchMailBox(command);
                        lock.notifyAll();
                    }
                    break;

                case Constants.COMMAND_SEND_MAIL:
                    synchronized (lock) {
                        clientOutputStream.writeObject(new MailWrapper(mail,Constants.COMMAND_SEND_MAIL, appModel.getUsername().get()));
                        lock.notifyAll();
                    }
                    break;

                case Constants.COMMAND_DELETE_MAIL:
                    synchronized (lock) {
                        clientOutputStream.writeObject(new MailWrapper(mail, Constants.COMMAND_DELETE_MAIL, appModel.getUsername().get()));
                        lock.notifyAll();
                    }
                    break;

                case Constants.COMMAND_CHECK_NEW_MAIL_PRESENCE:
                    synchronized(lock) {
                        clientOutputStream.writeObject(new MailWrapper(Constants.COMMAND_CHECK_NEW_MAIL_PRESENCE, appModel.getUsername().get()));
                        checkNewMail();
                        lock.notifyAll();
                    }
                    break;

                default:

                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                this.socket.shutdownInput();
                this.socket.shutdownOutput();
                this.socket.close();
                synchronized (lock) {
                    lock.notifyAll();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * this function checks for new emails, and then shows an alert when new emails are found
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void checkNewMail() throws IOException, ClassNotFoundException{
        Integer count = (Integer)clientInputStream.readObject();

        if(count>0){
            Utils.showAlert(AlertType.INFORMATION, "You have " + count + " new messages!", "Attention:", "You have new messages!");
        }  
    }

    /**
     * this function fetches a mailbox from the remote server
     * @param type the type (inbox / sent / deleted ) defined in Constants class
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void fetchMailBox(int type) throws IOException, ClassNotFoundException {
        clientOutputStream.writeObject(new MailWrapper(type, appModel.getUsername().get()));
        MailWrapper mw = (MailWrapper) clientInputStream.readObject();
        appModel.setCurrentMailFolder(ClientModel.convertArrayListToObservableList(mw.getMailsFolder()));
    }

    /**
     * This function inisitlaizes a socket
     * @param endpoint
     * @param port
     */
    private void initialiseSocket(String endpoint, String port) {
        try {
            socket = new Socket(endpoint, Integer.parseInt(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function initializes input and output streams
     */
    private void initialiseStreams() {
        try {
            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
