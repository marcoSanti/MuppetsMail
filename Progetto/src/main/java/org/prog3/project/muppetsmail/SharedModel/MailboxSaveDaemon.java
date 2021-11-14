package org.prog3.project.muppetsmail.SharedModel;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MailboxSaveDaemon extends Thread {
    private ArrayList<MailBox> mailBoxes;
    private int sleepDelaysMs;

    public MailboxSaveDaemon(ArrayList<MailBox> mailBoxes, int sleepDelayMs) {
        super("Save Files Daemon");
        this.mailBoxes = mailBoxes;
        this.sleepDelaysMs = sleepDelayMs;
        setDaemon(true);
        start();
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
