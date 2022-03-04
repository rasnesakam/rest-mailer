package org.em.main.services;

import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PropertiesManager {
    public static int
            SMTP_GMAIL = 0,
            SMTP_OUTLOOK = 1,
            SMTP_YANDEX = 3;
    private static final String[] hosts = {
            "smtp.gmail.com"
    };
    private static final String[] ports = {
            "465"
    };

    public Properties getProperties(int type){
        Properties properties = new Properties();
        properties.put("mail.smtp.host",hosts[type]);
        properties.put("mail.smtp.port",ports[type]);
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.imap.sasl.mechanisms", "XOAUTH2");
        properties.put("mail.imap.auth.login.disable", "true");
        properties.put("mail.imap.auth.plain.disable", "true");
        properties.put("mail.smtp.auth","true");
        return properties;
    }

    public int getType(String mail){
        int first = mail.indexOf('@');
        String host = mail.substring(first + 1);
        switch (host){
            case "gmail.com":
                return 0;
            case "yahoo.com":
                return 1;
            case "outlook.com":
                return 2;
            case "hotmail.com":
                return 3;
            default:
                return -1;
        }
    }
}
