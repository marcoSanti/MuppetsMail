package org.prog3.project.muppetsmail.SharedModel;

import java.io.Serializable;
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

    public Mail(String mailId, String from, ArrayList<String> to, String message, String subject, int currentMailBox) {
        this.mailId = mailId;
        this.from = from;
        this.to = to;
        this.message = message;
        this.subject = subject;
        this.date = new Date();
        this.currentMailBox = currentMailBox;
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
        long todayDiff = date.getTime() - today.getDate();
        if(TimeUnit.HOURS.toHours(todayDiff) > 24){
            return "[" + dtfOld.format(date) + "]" + "    from: " + from + "    subject: " + subject;
        }else{
            return "[" + dtfToday.format(date) + "]" + "    from: " + from + "    subject: " + subject;
        }
    }
}
