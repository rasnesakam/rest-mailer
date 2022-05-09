package org.em.main.gmail;

import org.em.main.shared.Mail;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "gmail")
public class GmailController {

    @PostMapping
    public String send(@RequestBody Mail mail){
        return String.format("From: %s\n<br>To: %s",mail.getFrom(), mail.getTo());
    }
}
