package org.em.main.gmail;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.em.main.shared.Mail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GmailService {
    private GmailAdapter adapter;

    @Autowired
    public GmailService(GmailAdapter adapter){
        this.adapter = adapter;
    }

    public void sendMail(Mail mail) throws GeneralSecurityException, IOException, MessagingException {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = adapter.createService(transport,mail.getFrom());
        MimeMessage mimeMessage = adapter.createEmail(
                mail.getFrom(), mail.getTo(), mail.getCc(), mail.getSubject(), mail.getMessage()
        );
        Message message = adapter.createMessage(mimeMessage);
        service.users().messages().send(mail.getFrom(),message);
    }
}
