package org.em.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MailSendException extends RuntimeException{

    public MailSendException(String message){
        super(message);
    }
    public MailSendException(String message, Throwable cause){
        super(message,cause);
    }
}
