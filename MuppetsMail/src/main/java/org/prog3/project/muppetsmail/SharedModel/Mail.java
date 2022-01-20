package org.prog3.project.muppetsmail.SharedModel;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class rappresents a single email
 */
public class Mail implements Serializable {
    private String mailId;
    private String from;
    private ArrayList<String> to;
    private String message;
    private String subject;
    private Date date;
    private boolean isNew;
    private final DateFormat dateFormatterOld = new SimpleDateFormat("dd/MM/yy");
    private final DateFormat dateFormatterToday = new SimpleDateFormat("HH:mm");
    private int currentMailBox;

    /**
     * 
     * @param from user wich sent the email
     * @param to users to which an email should be sent
     * @param message the message
     * @param subject the subject
     * @param currentMailBox the current mailbox in which the email is located
     */
    public Mail(String from, ArrayList<String> to, String message, String subject, int currentMailBox) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.subject = subject;
        this.date = new Date();
        this.currentMailBox = currentMailBox;
        this.isNew = true;
        try{
            String hashSource = new String(from+to.toString()+message+subject+date.toString());
            mailId = getMailHash(hashSource);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

    }

    /**
     * 
     * @return the current mailbox in which the email is located
     */
    public int getCurrentMailBox() {
        return currentMailBox;
    }

    /**
     * 
     * @param currentMailBox the new mailbox in which the email is located
     */
    public void setCurrentMailBox(int currentMailBox) {
        this.currentMailBox = currentMailBox;
    }

    /**
     * 
     * @return the email id
     */
    public String getMailId(){
        return mailId;
    }

    /**
     * 
     * @return the email sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * 
     * @return an array list contining all the email recipients
     */
    public ArrayList<String> getTo() {
        return to;
    }

    /**
     * 
     * @return the email message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @return the email subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 
     * @return the date of when the email was sent
     */
    public Date getDate() {
        return date;
    }

    /**
     * 
     * @return whether the email has just been recived
     */
    public boolean isMailNew() {
        return this.isNew;
    }

    /**
     * this function sets the mail as read (not new)
     */
    public void setMailAsRead(){
        this.isNew = false;
    }


    /**
     * This methods is not anymore used but it is left because if we want to revert it will be possibile
     * This method prints the email in a string formats. it was used in the client listView to show the email as an item
     */
    @Override
    public String toString() {
        Date today = new Date();
        
        long todayDiff = date.getTime() - today.getTime();
        if(TimeUnit.HOURS.toHours(todayDiff) > 24){
            return "[" + dateFormatterOld.format(date) + "]" + "    from: " + from + "    subject: " + subject;
        }else{
            return "[" + dateFormatterToday.format(date) + "]" + "    from: " + from + "    subject: " + subject;
        }
    }


    /**
     * This functions clones the email, so that when an email is sent to multiple recipients, it can be 
     * cloned to evry one of them
     */
    public Mail clone(){
        String fromClone = new String(this.from);
        ArrayList<String> toClone = new ArrayList<>();
        for(String ToCloneElement : to){
            toClone.add(new String(ToCloneElement));
        }
        String messageClone = new String(this.message);
        String subjectClone = new String(this.subject);
        int currentMbClone = this.currentMailBox;

        return new Mail( fromClone, toClone, messageClone, subjectClone, currentMbClone);
    }

    /**
     * This function creates the hash of the contents of the email to use as a mail id
     * @param source
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String getMailHash(String source) throws NoSuchAlgorithmException{
 
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hash = digest.digest(source.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hash.length);

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
