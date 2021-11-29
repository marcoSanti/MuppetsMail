package org.prog3.project.muppetsmail.Client.Controller;

import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.Client.Model.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.constant.Constable;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ConnectionManager {
    private String server;
    private int port;
    Socket socket = null;
    ExecutorService executors;
    ClientModel clientModel;

    public ConnectionManager(String server, int port, ClientModel model) {
        this.server = server;
        this.port = port;
        this.clientModel = model;
        this.executors = Executors.newFixedThreadPool(Constants.NUM_OF_THREADS);
    }

    public boolean connectToServer() {
        boolean connectionEstablished = false;
        try {
            socket = new Socket("127.0.0.1", port);
            connectionEstablished = true;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectionEstablished;
    }

    public void runTask(int command, Object lock){
        NetworkTask nt = new NetworkTask(clientModel, command, lock);
        executors.execute(nt);
    }

    public void runTask(int command, Object lock, Mail mailToBeSent){
        NetworkTask nt = new NetworkTask(clientModel, command, lock, mailToBeSent);
        executors.execute(nt);
    }


    public void shotDownConnection(){this.executors.shutdown();}

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
