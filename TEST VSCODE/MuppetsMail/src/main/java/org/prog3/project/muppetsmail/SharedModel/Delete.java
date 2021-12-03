package org.prog3.project.muppetsmail.SharedModel;

import java.io.Serializable;

public class Delete implements Serializable {
    private Mail mail;

    public Delete(Mail mail) {
        this.mail = mail;
    }

    public Mail getMail() {
        return mail;
    }
}
