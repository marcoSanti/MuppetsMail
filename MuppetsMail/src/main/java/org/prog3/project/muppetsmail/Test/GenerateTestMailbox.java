package org.prog3.project.muppetsmail.Test;

import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GenerateTestMailbox {
    public static void main(String[] args) {
        ArrayList<String> usernames= new ArrayList<>();
        usernames.add("Marco");
        usernames.add("Nicolo");
        usernames.add("Nausica");
        usernames.add("Giulia");

        MailBox mbox;


        try {
            Files.createDirectories(Paths.get("./ServerMailBoxes"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String s: usernames){
            try {
                mbox = new MailBox(s);
                mbox.createOutputObjectWriter("./ServerMailBoxes/" + s + ".muppetsmail");
                mbox.generateObservableItems();
                ArrayList<String> to = new ArrayList<>();
                to.add("noreply.demo");

                for(int i=0; i<10;i++){

                    mbox.addMail(new Mail("i"+i, "testEmail", to, "Test email of inbox", "inbox Test"+i+ " for " + s, 1), 1);
                    mbox.addMail(new Mail("d"+i, "testEmail", to, "Test email of sent", "sent Test"+i+ " for " + s, 2), 2);
                    mbox.addMail(new Mail("s"+i, "testEmail", to, "Test email of deleted", "deleted Test"+i+ " for " + s, 3), 3);
                }
                mbox.saveToDisk();
            }catch(IOException  e){
                e.printStackTrace();
            }
        }

        System.out.println("Mailboxes generated!");
    }

}
