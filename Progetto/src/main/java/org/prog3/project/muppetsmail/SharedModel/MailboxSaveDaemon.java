package org.prog3.project.muppetsmail.SharedModel;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MailboxSaveDaemon extends Thread {
    private ArrayList<MailBox> mailBoxes;
    private int sleepDelaysMs;
    private String mailBoxPath;

    public MailboxSaveDaemon(ArrayList<MailBox> mailBoxes, int sleepDelayMs, String mailBoxPath) {
        super("Save Files Daemon");
        this.mailBoxes = mailBoxes;
        this.sleepDelaysMs = sleepDelayMs;
        this.mailBoxPath = mailBoxPath;
        setDaemon(true);
        createOutputStream();
        start();
    }

    private void createOutputStream() {

       try {
           Files.createDirectories(Paths.get(mailBoxPath));
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           System.out.println("LOG MESSAGE IN CREATEOUTPUTSTREAM: Stream created");
       }
    }

    public void run() {
        while(true) {
            try {
                for(MailBox m : mailBoxes) {
                    m.writeToDisk();
                    System.out.println("Written to disk mailbox of: " + m.getUsername());
                }
                sleep(sleepDelaysMs);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }


    }
}
