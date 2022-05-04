package org.em.main.mailers.gmail;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.em.main.adapters.concretes.GMailAdapter;
import org.em.main.modals.Mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class GmailService {
    private Credential credential;
    private Message message;
    private Gmail service;

    public static class Builder{

        private static final String APP_NAME = "Rest Mailer";
        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        private static final String TOKEN_STORE = "tokens";
        private static final List<String> SCOPE = Collections.singletonList(GmailScopes.GMAIL_LABELS);
        private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

        private GmailService gmail;

        public Builder(){
            gmail = new GmailService();
        }

        public Builder withCredentials(String userId, final NetHttpTransport transport) throws IOException {
            InputStream in = GMailAdapter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null)
                throw new FileNotFoundException(String.format("Resource not found: %s",CREDENTIALS_FILE_PATH));
            GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                    .Builder(transport,JSON_FACTORY,secrets,SCOPE)
                    .setDataStoreFactory(new FileDataStoreFactory(new File(TOKEN_STORE)))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

            gmail.credential = new AuthorizationCodeInstalledApp(flow,receiver).authorize(userId);
            return this;
        }

        private MimeMessage createMimeMessage(Mail mail) throws MessagingException {
            Properties props = new Properties();

            Session session = Session.getInstance(props,null);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(mail.getFrom());
            message.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(mail.getTarget()));
            InternetAddress[] ccs = new InternetAddress[
                    Optional.ofNullable(mail.getCc()).orElse(new ArrayList<>()).size()
                    ];
            for (int i = 0; i < ccs.length; i++){
                ccs[i] = new InternetAddress(mail.getCc().get(i));
            }
            message.setRecipients(javax.mail.Message.RecipientType.CC,ccs);
            message.setSubject(mail.getSubject());

            message.setContent(mail.getMessage(),"text/html; charset=utf-8");
            return message;
        }

        public Builder withMessage(Mail mail) throws MessagingException, IOException {
            MimeMessage mimeMessage = createMimeMessage(mail);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mimeMessage.writeTo(stream);
            byte[] bytes = stream.toByteArray();
            String encodedMail = Base64.encodeBase64URLSafeString(bytes);
            com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
            message.setRaw(encodedMail);
            gmail.message = message;
            return this;
        }

        public GmailService build() throws GeneralSecurityException, IOException {
            final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(transport,JSON_FACTORY, gmail.credential)
                    .setApplicationName(APP_NAME).build();
            gmail.service = service;
            return gmail;
        }
    }

    public Gmail.Users.Messages.Send sendMail(String userId) throws GeneralSecurityException, IOException {
        return service.users().messages().send(userId,message);
    }

}
