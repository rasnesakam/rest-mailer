package org.em.main;

import org.em.main.webconf.JsonProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.stream.Stream;

@SpringBootApplication
@ComponentScan(basePackageClasses = { MainClass.class,JsonProps.class})
public class MainClass {
    public static void main(String[] args) {
        SpringApplication.run(MainClass.class,args);
    }
}
