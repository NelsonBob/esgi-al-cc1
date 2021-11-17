package org.sid.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.sid.entity.Mail;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    /*  @Autowired
     private SpringTemplateEngine templateEngine;
  */
    public void sendEmail(Mail mail) {

        final String username = "koumwinnie@zohomail.com";
        final String password = "Serge$Winnie";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.zoho.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.transport.protocol", "SMTP");
        prop.put(" mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Context context = new Context();
            context.setVariables(mail.getModel());
            // String html = templateEngine.process("email/email-template", context);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFrom()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail.getTo())
            );
            message.setSubject(mail.getSubject());
            message.setText(mail.getText());

            Transport.send(message);

            //log.info("Message send !");
            System.out.println("Message send !");
        } catch (MessagingException e) {
            // log.error(e.getMessage(), e);
            System.err.println(e.getMessage());
        }
    }

}
