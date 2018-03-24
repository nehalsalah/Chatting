/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Generic MailAPI class for sending a mail using JavaMail API
 *
 * @author EslamWaheed
 */

public class MailAPI {

    private String from;
    private String to;
    private String password;
    private String subject;
    private String text;

//    public static final String serverMail = "JustForDevelopingTest@gmail.com";
//    public static final String serverPassword = "80#7test*15";

    /**
     *
     */
    
    public MailAPI() {
        
    }

    /**
     * public constructor for MailAPI that take all the data used to send the mail
     *
     * @param from
     * @param to
     * @param password
     * @param subject
     * @param text
     */
    public MailAPI(String from, String to, String password, String subject, String text) {
        this.from = from;
        this.to = to;
        this.password = password;
        this.subject = subject;
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @param mailAPI
     */
    public void sendMail(MailAPI mailAPI) {

        Properties props = new Properties();
        props.put("mail.smtp.user", "username");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");

        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailAPI.from, mailAPI.password);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(mailAPI.from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailAPI.to));
            message.setSubject(mailAPI.subject);
            message.setText(mailAPI.text);
            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(MailAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MailAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
