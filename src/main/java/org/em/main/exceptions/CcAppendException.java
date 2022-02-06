package org.em.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONTINUE)
public class CcAppendException extends RuntimeException{
    public CcAppendException(String message){
        super(message);
    }
}
