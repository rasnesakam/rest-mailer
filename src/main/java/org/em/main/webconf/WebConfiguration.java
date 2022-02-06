package org.em.main.webconf;

import org.em.main.adapters.abstracts.MailAdapter;
import org.em.main.adapters.concretes.BasicMailAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class WebConfiguration {


    @Bean
    MailAdapter getBasicMailAdapter(){
        return new BasicMailAdapter(defaultProperties());
    }
    @Bean
    Properties defaultProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");
        return properties;
    }
}
