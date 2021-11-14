package org.prog3.project.muppetsmail.SharedModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MailboxSaveDaemon extends Thread {
    private MailBox mailBox;
    private int sleepDelaysMs;
    private ObjectOutputStream writer;
    private String mailboxUserName;
    private String mailBoxPath;

    public MailboxSaveDaemon(MailBox mailBox, int sleepDelayMs, String mailBoxUserName, String mailBoxPath) {
        super("Save Files Daemon");
        this.mailBox = mailBox;
        this.sleepDelaysMs = sleepDelayMs;
        this.mailboxUserName = mailBoxUserName;
        this.mailBoxPath = mailBoxPath;
        setDaemon(true);
        createOutputStream();
    }

    private void createOutputStream() {

       try {
           Files.createDirectories(Paths.get(mailBoxPath));
           writer = new ObjectOutputStream(new FileOutputStream(mailBoxPath + mailboxUserName + ".muppetsMailBox"));
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           System.out.println("LOG MESSAGE IN CREATEOUTPUTSTREAM: Stream created");
       }
    }

    public void run() {
        while(true) {
            try {
                System.out.println("Saving mailbox to " + mailboxUserName + ".dat");
                writer.writeObject(mailBox);
                sleep(sleepDelaysMs);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {

            }
        }


    }
}
