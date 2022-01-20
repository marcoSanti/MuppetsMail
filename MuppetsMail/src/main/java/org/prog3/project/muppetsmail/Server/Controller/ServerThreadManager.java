package org.prog3.project.muppetsmail.Server.Controller;

import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import org.prog3.project.muppetsmail.SharedModel.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is a manager for the serverThread.
 */
public class ServerThreadManager implements Runnable {

    private ServerModel serverModel;
    ServerSocket server =null;
    ExecutorService threadPool = null;
    private boolean isRunning = false;

    /**
     * this function initializes the threadPool to a new scheduledThreadPool with NUM_OF_THREADS threads
     * @param serverModel the model of the server
     */
    public ServerThreadManager(ServerModel serverModel) {
        this.serverModel = serverModel;
        threadPool = Executors.newScheduledThreadPool(Constants.NUM_OF_THREADS);
    }

    /**
     * This function stops the threadpool and updates the status of the server. 
     */
    public void stopServer(){
        threadPool.shutdown();
        try {
            this.isRunning = false;
            server.close();
        } catch (IOException e) {
            Utils.addLogToGUI("IOException, something went wrong...", e.getMessage(),serverModel);
        } catch (NullPointerException e) {
            Utils.addLogToGUI("Unable to stop server. Maybe it is already stopped.", e.getMessage() + "@" + e.toString(),serverModel);
        }
    }

    /**
     * this function is the run method of the thread. it creates the socket and sets it running, then remains listening for incoming connection
     * and when a connection is recived, a task is creaded and passed to the thread pool to be executed
     */
    @Override
    public void run() {
        try {
            this.createServerSocketAndSetIsRunning();
            while(isRunning){
                Socket socket = server.accept();
                threadPool.execute(new ServerThread(socket, serverModel));
            }
        } catch (IOException e) {
            Utils.addLogToGUI(e.getMessage(), e.toString(),serverModel);
            System.out.println("SOCKET CLOSED");
        }finally {
            this.isRunning = false;
            if(server != null && !server.isClosed()) stopServer();
        }
    }

    /**
     * This function creates a server socket on the defined server port and set the status of the server to running
     * @throws IOException
     */
    private void createServerSocketAndSetIsRunning() throws IOException {
        server = new ServerSocket(Constants.SERVER_PORT);
        Utils.addLogToGUI("Server started on port " + Constants.SERVER_PORT,serverModel);
        this.isRunning = true;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
