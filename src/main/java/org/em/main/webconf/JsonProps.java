package org.em.main.webconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@PropertySource(
        value = "classpath:credentials.json",
        factory = JsonPropsFactory.class
)
@ConfigurationProperties
public class JsonProps {
    private LinkedHashMap<String, ?> web;

    public LinkedHashMap<String, ?> getWeb() {
        return web;
    }

    public void setWeb(LinkedHashMap<String, ?> web) {
        this.web = web;
    }
}
