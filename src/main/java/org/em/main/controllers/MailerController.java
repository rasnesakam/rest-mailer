package org.em.main.controllers;

import org.em.main.adapters.abstracts.MailAdapter;
import org.em.main.adapters.concretes.BasicMailAdapter;
import org.em.main.modals.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MailerController {
    private MailAdapter adapter;

    @Autowired
    public MailerController(MailAdapter adapter) {
        this.adapter = adapter;
    }

    @PostMapping
    public String singleMail(@RequestBody Mail mail){

        adapter.setMail(mail).send();
        return "Mail gönderildi";
    }

    @PostMapping("/multiple")
    public String multipleMail(@RequestBody List<Mail> mails){
        return "";
    }

    @GetMapping
    public String getIndex(){
        return "Hello";
    }
}
