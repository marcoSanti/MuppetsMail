package org.prog3.project.muppetsmail.Test;


import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;
import org.prog3.project.muppetsmail.SharedModel.MailboxSaveDaemon;

import java.util.ArrayList;

public class TestMailBoxSavedDaemon {
    public static void main(String[] args) {
        String username = "Pippo";
        int secondsToSlepp = 1500;
        MailBox mailbox = new MailBox(username);
        MailboxSaveDaemon mailboxSaveDaemon = new MailboxSaveDaemon(mailbox, secondsToSlepp, username);

        mailboxSaveDaemon.start();
        try{
            mailboxSaveDaemon.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createEmails() {
    }
}
