package org.prog3.project.muppetsmail.Server.Controller;

import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Ciaoooooo");
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Sono morto");
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
