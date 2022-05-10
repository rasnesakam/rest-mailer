package org.em.main.gmail;

import org.em.main.shared.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping(path = "gmail")
public class GmailController {
    private GmailService service;

    @Value("${gmailapi.client_id}")
    private static String CLIENT_ID;

    @Value("${gmailapi.redirect_uri")
    private static String REDIRECT_URI;

    @Autowired
    public GmailController(GmailService service){
        this.service = service;
    }

    @PostMapping
    public String send(@RequestBody Mail mail){
        try {
            service.sendMail(mail);
            return "ok";
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            return e.getMessage();
        }
    }

    @GetMapping(path = "auth/{email}")
    public RedirectView getToken(RedirectAttributes attrs, @PathVariable("email") String email){
        String url = "https://accounts.google.com/o/oauth2/v2/auth?";
        url = url.concat("scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly&");
        url = url.concat("access_type=offline&");
        url = url.concat("include_granted_scopes=true&");
        url = url.concat("response_type=code&");
        url = url.concat("state=state_parameter_passthrough_value&");
        url = url.concat(String.format("redirect_uri=%s&",REDIRECT_URI));
        url = url.concat(String.format("login_hint=%s&",email));
        url = url.concat(String.format("client_id=%s",CLIENT_ID));
        return new RedirectView(url);
    }
}
