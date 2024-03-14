/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kazcables.util;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
public class Mail extends Properties{
    
    public static void SendMail(String from, String to, String subject, String text) throws MessagingException{
        Properties prop = System.getProperties();
        String host = "localhost";
        prop.setProperty("mail.stmp.host", host);
        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.port", "1025");
        prop.put("mail.smtp.auth", "false");
        Session emailingSession = Session.getDefaultInstance(prop);
        try{
            MimeMessage m = new MimeMessage(emailingSession);
            m.setFrom(new InternetAddress(from));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject((String)subject);
            m.setText(text);
            
            Transport.send(m);
            
        }
        catch(MessagingException ex){
            ex.printStackTrace();
        }
    }
}
