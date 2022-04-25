package com.ryoko.rent.enviroment;

import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Data
public class EmailEnvironmentBuilder {
    public final String SIMPLE_MAIL_TRANSFER_PROTOCOL;
    public final String USERNAME;
    public final String PASSWORD;
    public final String FROM_EMAIL;
    public final String EMAIL_SUBJECT;
    public final String GMAIL_SMTP_SERVER;
    public final String SMTP_HOST;
    public final String SMTP_AUTH;
    public final String SMTP_PORT;
    public final int DEFAULT_PORT;
    public final String SMTP_STARTTLS_ENABLE;
    public final String SMTP_STARTTLS_REQUIRED;

    public EmailEnvironmentBuilder(Environment e) {
        this.SIMPLE_MAIL_TRANSFER_PROTOCOL = e.getRequiredProperty("email.verification.simple_mail_transfer_protocol");
        this.USERNAME = e.getRequiredProperty("email.verification.username");
        this.PASSWORD = e.getRequiredProperty("email.verification.password");
        this.FROM_EMAIL = e.getRequiredProperty("email.verification.from_email");
        this.EMAIL_SUBJECT = e.getRequiredProperty("email.verification.email_subject");
        this.GMAIL_SMTP_SERVER = e.getRequiredProperty("email.verification.gmail_smtp_server");
        this.SMTP_HOST = e.getRequiredProperty("email.verification.smtp_host");
        this.SMTP_AUTH = e.getRequiredProperty("email.verification.smtp_auth");
        this.SMTP_PORT = e.getRequiredProperty("email.verification.smtp_port");
        this.DEFAULT_PORT = Integer.parseInt(e.getRequiredProperty("email.verification.default_port"));
        this.SMTP_STARTTLS_ENABLE = e.getRequiredProperty("email.verification.smtp_starttls_enable");
        this.SMTP_STARTTLS_REQUIRED = e.getRequiredProperty("email.verification.smtp_starttls_required");
    }

}
