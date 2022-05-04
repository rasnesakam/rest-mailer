package org.em.main.adapters.concretes;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.apache.commons.codec.binary.Base64;
import org.em.main.modals.Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GMailAdapter {
    private static final String APP_NAME = "Rest Mailer";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKEN_STORE = "tokens";
    private static final List<String> SCOPE = Collections.singletonList(GmailScopes.GMAIL_LABELS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException{
        InputStream in = GMailAdapter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null)
            throw new FileNotFoundException(String.format("Resource not found: %s",CREDENTIALS_FILE_PATH));
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport,JSON_FACTORY,secrets,SCOPE)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKEN_STORE)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow,receiver).authorize("user");
    }

    public MimeMessage createMessage(Mail mail) throws MessagingException {
        Properties props = new Properties();

        Session session = Session.getInstance(props,null);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(mail.getFrom());
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(mail.getTarget()));
        InternetAddress[] ccs = new InternetAddress[mail.getCc().size()];
        for (int i = 0; i < ccs.length; i++){
            ccs[i] = new InternetAddress(mail.getCc().get(i));
        }
        message.setRecipients(Message.RecipientType.CC,ccs);
        message.setSubject(mail.getSubject());

        message.setContent(mail.getMessage(),"text/html; charset:utf-8");
        return message;
    }

    public com.google.api.services.gmail.model.Message createMessagewithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        emailContent.writeTo(stream);
        byte[] bytes = stream.toByteArray();
        String encodedMail = Base64.encodeBase64URLSafeString(bytes);
        com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
        message.setRaw(encodedMail);
        return message;
    }

    public com.google.api.services.gmail.model.Message sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {
        com.google.api.services.gmail.model.Message message = createMessagewithEmail(emailContent);
        message = service.users().messages().send(userId,message).execute();
        return message;
    }
}
