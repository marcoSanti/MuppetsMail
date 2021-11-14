package org.prog3.project.muppetsmail.Test;


import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;
import org.prog3.project.muppetsmail.SharedModel.MailboxSaveDaemon;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestMailBoxSavedDaemon {
    public static void main(String[] args) {
        String username = "Pippo";
        int secondsToSlepp = 1500;

        ArrayList<MailBox> mailboxes = new ArrayList<>();
        ArrayList<String> usernames= new ArrayList<>();
        usernames.add("Marco");
        usernames.add("Nicolo");
        usernames.add("Nausica");
        usernames.add("Giulia");

        try {
            Files.createDirectories(Paths.get("./testMailBox"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String s: usernames){
            try {
                mailboxes.add(new MailBox(s, new ObjectOutputStream(new FileOutputStream("./testMailBox/" + s + ".muppetsmail"))));
            }catch(IOException e){
                e.printStackTrace();
            }
        }


        MailboxSaveDaemon mailboxSaveDaemon = new MailboxSaveDaemon(mailboxes, secondsToSlepp);


        try{
            mailboxSaveDaemon.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createEmails() {
    }
}
