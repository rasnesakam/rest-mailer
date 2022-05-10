package org.em.main.gmail;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.tomcat.util.buf.Utf8Encoder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
public class GmailAdapter {

    private static final String APP_NAME = "Rest Mailer";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIR = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS = "/credentials.json";

    private Credential getCredential(final NetHttpTransport transport, String user) throws IOException{

        InputStream stream = GmailAdapter.class.getResourceAsStream(CREDENTIALS);
        if (stream == null)
            throw new IOException(String.format("File not found: &s",CREDENTIALS));

        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,new InputStreamReader(stream));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(transport,JSON_FACTORY,secrets,SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIR)))
                .setAccessType("online")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();


        Credential credential = new AuthorizationCodeInstalledApp(flow,receiver).authorize(user);
        return credential;
    }
    public MimeMessage createEmail(String from, String to, String[] cc, String subject, String body)
        throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props,null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to));
        InternetAddress[] addresses = new InternetAddress[cc.length];
        for (int i = 0; i < addresses.length; i++)
            addresses[i] = new InternetAddress(cc[i]);
        email.setRecipients(javax.mail.Message.RecipientType.CC,addresses);
        email.setSubject(subject,"utf-8");
        email.setText(body,"utf-8");

        return email;
    }

    public Message createMessage(MimeMessage email) throws MessagingException, IOException{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedMail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedMail);
        return message;
    }

    public Gmail createService(NetHttpTransport transport, String user) throws IOException {
        Credential credential = getCredential(transport,user);
        return new Gmail.Builder(transport,JSON_FACTORY,credential)
                .setApplicationName(APP_NAME)
                .build();
    }
}
