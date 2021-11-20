package org.prog3.project.muppetsmail.Test;


import org.prog3.project.muppetsmail.SharedModel.Exceptions.MailBoxNotFoundException;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GenerateTestMailbox {
    public static void main(String[] args) {
        ArrayList<MailBox> mailboxes = new ArrayList<>();
        ArrayList<String> usernames= new ArrayList<>();
        usernames.add("Marco");
        usernames.add("Nicolo");
        usernames.add("Nausica");
        usernames.add("Giulia");

        MailBox mbox;

        try {
            Files.createDirectories(Paths.get("./testMailBox"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String s: usernames){
            try {
                mbox = new MailBox(s, new ObjectOutputStream(new FileOutputStream("./testMailBox/" + s + ".muppetsmail")));
                ArrayList<String> to = new ArrayList<>();
                to.add("noreply.demo");

                for(int i=0; i<10;i++){
                    System.out.println(i);
                    mbox.addMail(new Mail("i"+i, "testEmail", to, "Test email of inbox", "inbox Test for " + s), 1);
                    mbox.addMail(new Mail("d"+i, "testEmail", to, "Test email of deleted", "deleted Test for " + s), 2);
                    mbox.addMail(new Mail("s"+i, "testEmail", to, "Test email of sent", "sent Test for " + s), 3);
                }
                mbox.saveToDisk();
            }catch(MailBoxNotFoundException | IOException e){
                e.printStackTrace();
            }
        }


    }

}