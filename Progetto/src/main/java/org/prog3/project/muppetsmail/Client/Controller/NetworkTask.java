package org.prog3.project.muppetsmail.Client.Controller;

import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.Client.Model.Constants;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkTask implements Runnable {
    ObjectOutputStream clientOutputStream;
    ObjectInputStream clientInputStream;
    Socket socket = null;
    ClientModel appModel;
    Lock lock;
    Condition jobDone;
    int command;

    public NetworkTask(ClientModel appModel, int command, Lock lock) {
        this.appModel = appModel;
        this.initialiseSocket(appModel.getEndpoint().getValue(), appModel.getEndpointPort().getValue());
        this.initialiseStreams();
        this.command = command;
        this.lock = lock;
        jobDone = lock.newCondition();
    }

    @Override
    public void run() {
        lock.lock();
        try {
            switch (command) {
                case Constants.COMMAND_SEND_USERNAME:
                    lock.lock();
                    clientOutputStream.writeObject(appModel.getUsername().getValue());
                    MailBox mb = (MailBox) clientInputStream.readObject();
                    System.out.println(mb);
                    mb.createOutputObjectWriter("./ClientMailBoxes/" + mb.getUsername() + ".muppetsmail");
                    mb.saveToDisk();
                    break;

                case Constants.COMMAND_SEND_MAIL:

                    break;

                default:

                    break;
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            //jobDone.signal();
            lock.unlock();
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

    public Condition jobDone() {
        return this.jobDone;
    }
}
