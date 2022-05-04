package org.em.main;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.em.main.mailers.gmail.GmailService;
import org.em.main.modals.Mail;
import org.junit.jupiter.api.Test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class UnitTest {

    @Test
    public void sendMail(){
        try {
            Mail mail = new Mail();
            mail.setFrom("ensar.makas@gmail.com");
            mail.setTarget("rasnesakam@gmail.com");
            mail.setMessage("Yeni bir gün dogdu merhabaaa");
            mail.setSubject("Selam");
            GmailService service = new GmailService.Builder()
                    .withCredentials("ensar.makas@gmail.com", GoogleNetHttpTransport.newTrustedTransport())
                    .withMessage(mail)
                    .build();
            service.sendMail("me");
        } catch (IOException | GeneralSecurityException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
