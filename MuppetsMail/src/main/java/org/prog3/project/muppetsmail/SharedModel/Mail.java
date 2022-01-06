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

public class Mail implements Serializable {
    private String mailId;
    private String from;
    private ArrayList<String> to;
    private String message;
    private String subject;
    private Date date;
    private boolean isNew;
    private final DateFormat dtfOld = new SimpleDateFormat("dd/MM/yy");
    private final DateFormat dtfToday = new SimpleDateFormat("HH:mm");
    private int currentMailBox;

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

    public int getCurrentMailBox() {
        return currentMailBox;
    }

    public void setCurrentMailBox(int currentMailBox) {
        this.currentMailBox = currentMailBox;
    }
    public String getMailId(){
        return mailId;
    }

    public String getFrom() {
        return from;
    }

    public ArrayList<String> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public boolean isMailNew() {
        return this.isNew;
    }

    public void setMailAsRead(){
        this.isNew = false;
    }

    @Override
    public String toString() {
        Date today = new Date();
        
        long todayDiff = date.getTime() - today.getTime();
        if(TimeUnit.HOURS.toHours(todayDiff) > 24){
            return "[" + dtfOld.format(date) + "]" + "    from: " + from + "    subject: " + subject;
        }else{
            return "[" + dtfToday.format(date) + "]" + "    from: " + from + "    subject: " + subject;
        }
    }

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

    //generate the mail hash 
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
