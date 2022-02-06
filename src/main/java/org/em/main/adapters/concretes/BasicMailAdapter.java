package org.em.main.adapters.concretes;

import org.em.main.adapters.abstracts.MailAdapter;
import org.em.main.exceptions.CcAppendException;
import org.em.main.exceptions.MailSendException;
import org.em.main.modals.Mail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class BasicMailAdapter implements MailAdapter {

    private Mail mail;
    private final Properties properties;

    @Autowired
    public BasicMailAdapter(Properties properties){
        this.properties = properties;
    }

    @Override
    public MailAdapter setMail(Mail mail) {
        this.mail = mail;
        return this;
    }

    @Override
    public void send() {


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail.getFrom(),mail.getPassword());
            }
        });

        try {
            int[] missed = {0};

            // Create message
            Message message = new MimeMessage(session);

            // Set owner of message
            message.setFrom(new InternetAddress(mail.getFrom()));

            // Add recievers for mail
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTarget()));

            // Append cc's if they exists
            if (mail.getCc() != null){
                mail.getCc().forEach(cc ->{
                    try {
                        message.setRecipient(Message.RecipientType.CC,new InternetAddress(cc));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        missed[0]++;
                    }
                });
            }

            // Set Subject
            message.setSubject(mail.getSubject());

            // Set body part
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(mail.getMessage(),"text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            Transport.send(message);

            if (missed[0] > 0)
                throw new CcAppendException("Mail delivered but some cc could't appended");
        } catch (MessagingException e) {
            throw new MailSendException("E mail couldn't delivered",e);
        }
    }
}
