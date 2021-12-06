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

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(new String(from+to.toString()+message+subject+date.toString()).getBytes(StandardCharsets.UTF_8));
            mailId = bytesToHex(hash);
            System.out.println(mailId);
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

    //converts bytes to string. used to generate the mail id!
    private static String bytesToHex(byte[] hash) {
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
