package org.em.main.webconf;

import org.em.main.adapters.abstracts.MailAdapter;
import org.em.main.adapters.concretes.JavaxMailAdapter;
import org.em.main.services.PropertiesManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class WebConfiguration {


    @Bean
    MailAdapter getBasicMailAdapter(){
        return new JavaxMailAdapter(getPropertyManager());
    }

    @Bean
    PropertiesManager getPropertyManager(){
        return new PropertiesManager();
    }
}
