package org.em.main.gmail;

import com.google.api.services.gmail.GmailScopes;
import org.em.main.shared.Mail;
import org.em.main.webconf.JsonProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping(path = "/gmail")
public class GmailController {
    private GmailService service;

    @Autowired
    private JsonProps props;

    @Autowired
    public GmailController(GmailService service){
        this.service = service;
    }

    @GetMapping
    public String index(){
        return "Hi";
    }

    @PostMapping
    public String send(@RequestBody Mail mail){
        try {
            service.sendMail(mail);
            return "ok";
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            return e.getMessage().concat(mail.toString());
        }
    }


    @GetMapping(path = "auth")
    public RedirectView auth(RedirectAttributes attrs){
        List<String> clientUris = (List) props.getWeb().get("redirect_uris");
        String clientId = props.getWeb().get("client_id").toString();
        String url = "https://accounts.google.com/o/oauth2/v2/auth?";
        url = url.concat(String.format("scope=%s&",GmailScopes.GMAIL_SEND));
        url = url.concat("access_type=online&");
        url = url.concat("include_granted_scopes=true&");
        url = url.concat("response_type=code&");
        url = url.concat("state=state_parameter_passthrough_value&");
        url = url.concat(String.format("redirect_uri=%s&",clientUris.get(0)));
        //url = url.concat(String.format("login_hint=%s&",email));
        url = url.concat(String.format("client_id=%s",clientId));
        return new RedirectView(url);
    }

}
