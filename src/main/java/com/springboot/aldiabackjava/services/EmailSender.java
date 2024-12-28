package com.springboot.aldiabackjava.services;

import jakarta.mail.*;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class EmailSender {

    private final String username;
    private final String password;
    @Autowired
    public EmailSender(@Value("${mail}") String username,
                       @Value("${passwordmail}") String password) {
        this.username = username;
        this.password = password;
    }

    public void senMail(String mailTo, String subjetct, String message)  {
        Session session = setupJavaxSession(username, password);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type","text/HTML; charset=UTF-8");
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mailTo)
            );
            msg.setFrom(username);
            msg.setSubject(subjetct);
            msg.setContent(message,"text/html");
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    private static Session setupJavaxSession(String username, String password){
        return Session.getInstance(getPoperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private static Properties getPoperties(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        return props;
    }
}
