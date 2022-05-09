package org.em.main;

import org.em.main.gmail.GmailAdapter;
import org.em.main.gmail.GmailService;
import org.em.main.shared.Mail;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class UnitTest {

    @Test
    public void sendGMail(){
        GmailAdapter adapter = new GmailAdapter();
        GmailService service = new GmailService(adapter);
        Mail mail = new Mail();
        mail.setFrom("ensar.makas@gmail.com");
        mail.setTo("rasnesakam@gmail.com");
        mail.setCc(new String[0]);
        mail.setSubject("E posta başlığı");
        mail.setMessage("Yeni bir gun doguyor merhabaa");
        try {
            service.sendMail(mail);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
