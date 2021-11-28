package org.prog3.project.muppetsmail.Client.Controller;

import java.io.IOException;
import java.net.Socket;

public class ConnectionManager {
    private String server;
    private int port;
    Socket socket = null;

    public ConnectionManager(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public boolean connectToServer() {
        boolean connectionEstablished = false;
        try {
            socket = new Socket("127.0.0.1", port);
            connectionEstablished = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectionEstablished;
    }

    public void closeConnectionToServer() {
        try {
            socket.close();
            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
