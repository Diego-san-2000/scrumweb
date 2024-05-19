package com.tfg.scrumweb.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;

import com.tfg.scrumweb.entity.BacklogItem;
import com.tfg.scrumweb.entity.Persona;
import com.tfg.scrumweb.entity.Proyecto;
import com.tfg.scrumweb.exception.MessageNotSent;

import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {

    private EmailService() {
        throw new IllegalStateException("Utility class");
    }
    @Value("${spring.mail.username}")
    private static String username;
    @Value("${spring.mail.password}")
    private static String password;

    @Async
    public static void sendCorreo(Proyecto proyecto, List<BacklogItem> backlogItemList, List<Persona> listaAEnviar)
     throws jakarta.mail.MessagingException, MessageNotSent{
    
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            Address[] recipients = new Address[listaAEnviar.size()];

            for(int i=0; i<listaAEnviar.size(); i++)
                recipients[i] = new InternetAddress(listaAEnviar.get(i).getEmail());
            StringBuilder texto =  new StringBuilder();

            for(BacklogItem item: backlogItemList)
                texto.append(item.toStringEmail());

            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setSubject("New Increments done");
            message.setText("In the Sprint " + proyecto.getNumeroSprint() 
                + " the following Increments have been achieved:\n" + texto.toString() + ".");
            
            Transport.send(message);

        } catch (MessagingException e) {
            throw new MessageNotSent(e.toString());
        }
    }
}
