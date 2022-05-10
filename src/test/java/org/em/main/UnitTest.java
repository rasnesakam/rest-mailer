package org.em.main;

import org.em.main.gmail.GmailAdapter;
import org.em.main.gmail.GmailService;
import org.em.main.shared.Mail;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

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

    //@Test
    public void testBase64(){
        String initial = "Yeni bir gün doğuyor merhaba";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.writeBytes(initial.getBytes(StandardCharsets.UTF_8));
        assertEquals(stream.toString(StandardCharsets.UTF_8),initial);
    }
}
