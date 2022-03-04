package org.em.main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/oauth")
public class OAuthController {

    @GetMapping(path = "gmail/{uname}/{passwd}")
    public String gmailOAuth(@PathVariable String uname, @PathVariable String passwd){
        return uname.concat(" ").concat(passwd);
    }
}
