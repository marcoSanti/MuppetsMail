package org.prog3.project.muppetsmail.Server.Controller;

import org.prog3.project.muppetsmail.Server.Model.ServerModel;
import org.prog3.project.muppetsmail.SharedModel.Mail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            Object input = serverInputStream.readObject();

            if (input.getClass() == String.class) {
                String username = (String) input;
                System.out.println("Username received: " + username);
                serverOutputStream.writeObject(this.serverModel.getMailBox(username));
                System.out.println("Mailbox sent");
            } else if (input.getClass() == Mail.class) {
                Mail inputMail = (Mail) input;
                System.out.println("Email received: " + inputMail);

            } else {

                System.out.println("Error: unknown class");
            }


        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error0: " +e.getMessage());

        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error1: " +e.getMessage());
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
