package org.prog3.project.muppetsmail.Client.Controller;

import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.Client.Model.Constants;
import org.prog3.project.muppetsmail.SharedModel.Delete;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkTask implements Runnable{
    ObjectOutputStream clientOutputStream;
    ObjectInputStream clientInputStream;
    Socket socket = null;
    ClientModel appModel;
    final Object lock;
    int command;
    Mail mail;

    public NetworkTask(ClientModel appModel, int command, Object lock) {
        this.appModel = appModel;
        this.initialiseSocket(appModel.getEndpoint().getValue(), appModel.getEndpointPort().getValue());
        this.initialiseStreams();
        this.command = command;
        this.lock = lock;
    }

    public NetworkTask(ClientModel appModel, int command, Object lock, Mail mail) {
        this(appModel, command, lock);
        this.mail = mail;
    }

    @Override
    public void run() {

        try {
            switch (command) {
                case Constants.COMMAND_SEND_USERNAME:
                    synchronized (lock){
                        clientOutputStream.writeObject(appModel.getUsername().getValue());
                        MailBox mb = (MailBox) clientInputStream.readObject();
                        mb.createOutputObjectWriter("./ClientMailBoxes/" + mb.getUsername() + ".muppetsmail");
                        mb.saveToDisk();
                        lock.notifyAll();
                    }
                    break;

                case Constants.COMMAND_SEND_MAIL:
                    synchronized (lock) {
                        clientOutputStream.writeObject(this.mail);
                        lock.notifyAll();
                    }
                    break;

                case Constants.COMMAND_DELETE_MAIL:
                    synchronized (lock) {
                        clientOutputStream.writeObject(new Delete(mail));
                        lock.notifyAll();
                    }
                    break;

                default:

                    break;
            }
        }catch (IOException | ClassNotFoundException e){
            System.out.println(e.getMessage());
            synchronized (lock){
                lock.notifyAll();
            }
        }finally {
            try {
                this.socket.shutdownInput();
                this.socket.shutdownOutput();
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialiseSocket(String endpoint, String port) {
        try {
            socket = new Socket(endpoint, Integer.parseInt(port));
            System.out.println("Socket created in thread");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialiseStreams() {
        try {
            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
