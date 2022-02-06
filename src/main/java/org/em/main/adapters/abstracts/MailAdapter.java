package org.em.main.adapters.abstracts;

import org.em.main.modals.Mail;

public interface MailAdapter {
    MailAdapter setMail(Mail mail);
    void send();
}
