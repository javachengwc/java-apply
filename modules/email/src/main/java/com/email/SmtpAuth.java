package com.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuth extends Authenticator {
    private String username,password;    
   
    public SmtpAuth(String username,String password){    
        this.username = username;     
        this.password = password;     
    }    
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username,password);
    }    
} 
