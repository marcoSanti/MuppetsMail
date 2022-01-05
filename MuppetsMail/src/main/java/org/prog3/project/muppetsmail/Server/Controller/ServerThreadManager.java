package org.prog3.project.muppetsmail.Server.Controller;

import javafx.application.Platform;
import org.prog3.project.muppetsmail.Server.Model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThreadManager implements Runnable {

    private ServerModel serverModel;
    ServerSocket server =null;
    ExecutorService threadPool = null;
    private boolean isRunning = false;


    public ServerThreadManager(ServerModel serverModel) {
        this.serverModel = serverModel;
        threadPool = Executors.newScheduledThreadPool(5);
    }

    public void stopServer(){
        threadPool.shutdown();
        try {
            this.isRunning = false;
            server.close();
        } catch (IOException e) {
            this.addLogToGUI("IOException, something went wrong...", e.getMessage());
        } catch (NullPointerException e) {
            this.addLogToGUI("Unable to stop server. Maybe it is already stopped.", e.getMessage() + "@" + e.toString());
        }
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(1234);
            this.addLogToGUI("Server started on port 1234");
            this.isRunning = true;
            while(isRunning){
                Socket socket = server.accept();
                threadPool.execute(new ServerThread(socket, serverModel));
            }
        } catch (IOException e) {
            this.addLogToGUI(e.getMessage(), e.toString());
            System.out.println("SOCKET CLOSED");
        }finally {
            this.isRunning = false;
            if(server != null && !server.isClosed()) stopServer();
        }
    }

    private void addLogToGUI(String message) {
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


    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
