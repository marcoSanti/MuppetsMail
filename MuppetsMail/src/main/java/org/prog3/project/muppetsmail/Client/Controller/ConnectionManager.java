package org.prog3.project.muppetsmail.Client.Controller;

import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class, is the network manager, meaning that its function is to manage
 * the connections that the client will create with the server
 * 
 */

public class ConnectionManager {
    private String server;
    private int port;
    Socket socket = null;
    ExecutorService executors;
    ClientModel clientModel;

    /**
     * Creates the class and setup the connection
     * ths clarr requires the client model as It is used to update the status of the connection,
     *  once the connection has been verified to work.
     * 
     * @param server server endpoint address
     * @param port server endpoint port
     * @param model the client app model. 
     */
    public ConnectionManager(String server, int port, ClientModel model) {
        this.server = server;
        this.port = port;
        this.clientModel = model;
        this.executors = Executors.newFixedThreadPool(Constants.NUM_OF_THREADS);
    }

    /**
     * this function is used to check whether a network connection is working.
     * if not, then an exception is thrown and we are able to know that the 
     * connection is not valid
     * @return whether the connection is valid or not
     */
    public boolean connectToServer() {
        try {
            socket = new Socket(server, port);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } 
    }

    /**
     * This function is the api to allow the client to make calls to the server.
     * it is done, by creating a new networkTask and then execute the networkTask in a fixed thread pool.
     * @param command the command to be executed
     * @param lock the lock object used for process synchonization
     */
    public void runTask(int command, Object lock){
        NetworkTask nt = new NetworkTask(clientModel, command, lock);
        executors.execute(nt);
    }

    /**
     * This function is the api to allow the client to make calls to the server.
     * it is done, by creating a new networkTask and then execute the networkTask in a fixed thread pool.
     * this specific version allows to send an email object,
     * @param command the command to be executed
     * @param lock the lock object used for process synchonization
     * @param mailToBeSent the mail object wich will be sent to the server
     */
    public void runTask(int command, Object lock, Mail mailToBeSent){
        NetworkTask nt = new NetworkTask(clientModel, command, lock, mailToBeSent);
        executors.execute(nt);
    }

    /**
     * This funciton termninates the execution of the thread pool, effectively, stopping
     * the client from being able to make connections with the server.
     */
    public void shotDownConnection(){this.executors.shutdown();}

    /**
     * @return the enpoint address of the server
     */
    public String getServer() {
        return server;
    }

    /**
     * This function is used to change server endpoint address
     * @param server the new server endpoint
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the server endpoint port
     */
    public int getPort() {
        return port;
    }

    /**
     * This function is used to change the server endpoint port
     * @param port the new server endpoint port
     */
    public void setPort(int port) {
        this.port = port;
    }
}
