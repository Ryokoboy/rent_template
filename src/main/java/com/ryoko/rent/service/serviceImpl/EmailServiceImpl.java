package com.ryoko.rent.service.serviceImpl;

import com.ryoko.rent.enviroment.EmailEnvironmentBuilder;
import com.ryoko.rent.model.User;
import com.ryoko.rent.service.serviceInterface.EmailService;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailEnvironmentBuilder emailEnvironmentBuilder;

    @Autowired
    public EmailServiceImpl(EmailEnvironmentBuilder emailEnvironmentBuilder) {
        this.emailEnvironmentBuilder = emailEnvironmentBuilder;
    }

    @Override
    public void sendVerificationPinCode(User user, int pinCode) throws MessagingException {
        String text = String.format("Hello, dear %s %s, your verification pin code for registration is: %s", user.getName(), user.getSurname(), pinCode);
        Message message = createEmail(user.getEmail(), new Date(), text);
        sendEmail(message);
    }

    @Override
    public void sendMessage(String email, String text) throws MessagingException {
        Message message = createEmail(email, new Date(), text);
        sendEmail(message);
    }

    private void sendEmail(Message message) throws MessagingException {
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(emailEnvironmentBuilder.SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(emailEnvironmentBuilder.GMAIL_SMTP_SERVER, emailEnvironmentBuilder.USERNAME, emailEnvironmentBuilder.PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String email, Date date, String text) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(emailEnvironmentBuilder.FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setSubject(emailEnvironmentBuilder.EMAIL_SUBJECT);
        message.setText(text);
        message.setSentDate(date);
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(emailEnvironmentBuilder.SMTP_HOST, emailEnvironmentBuilder.GMAIL_SMTP_SERVER);
        properties.put(emailEnvironmentBuilder.SMTP_AUTH, true);
        properties.put(emailEnvironmentBuilder.SMTP_PORT, emailEnvironmentBuilder.DEFAULT_PORT);
        properties.put(emailEnvironmentBuilder.SMTP_STARTTLS_ENABLE, true);
        properties.put(emailEnvironmentBuilder.SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}

