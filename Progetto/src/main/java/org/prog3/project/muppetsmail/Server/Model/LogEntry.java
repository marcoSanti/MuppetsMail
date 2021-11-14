package org.prog3.project.muppetsmail.Server.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry implements Serializable {
    private final String message;
    private final Date timestamp;
    private final DateFormat dtf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    public LogEntry(String message) {
        this.message = message;
        timestamp = new Date();
    }

    @Override
    public String toString() {
        return "[" + dtf.format(timestamp) + "]  ->  " +this.message;
    }
}
