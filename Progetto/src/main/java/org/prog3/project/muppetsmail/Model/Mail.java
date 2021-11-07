package org.prog3.project.muppetsmail.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Mail implements Serializable {
    private String mailId;
    private String from;
    private ArrayList<String> to;
    private String message;
    private String subject;
    private Date sentDate;

    public Mail(String mailId, String from, ArrayList<String> to, String message, String subject, Date sentDate) {
        this.mailId = mailId;
        this.from = from;
        this.to = to;
        this.message = message;
        this.subject = subject;
        this.sentDate = sentDate;
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

    public Date getSentDate() {
        return sentDate;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", sentDate=" + sentDate +
                '}';
    }
}
