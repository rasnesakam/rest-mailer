package org.em.main.shared;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Component
public class Mail {

    private String from;
    private String to;
    private String[] cc;
    private String message;
    private String subject;
    private String charset;
    private File media;

    public Mail(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject(){
        return this.subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public File getMedia() {
        return media;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset){
        this.charset = charset;
    }

    public void setMedia(File media) {
        this.media = media;
    }


    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc=" + Arrays.toString(cc) +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", media=" + media +
                '}';
    }

}
