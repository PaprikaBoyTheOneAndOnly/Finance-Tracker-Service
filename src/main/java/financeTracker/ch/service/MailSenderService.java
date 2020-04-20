package financeTracker.ch.service;

import financeTracker.ch.model.MailType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailSenderService {
    @Value("${mail.password}")
    private String password;

    private final Logger logger;

    @Autowired
    public MailSenderService(Logger logger) {
        this.logger = logger;
    }

    public void sendEmail(String to, String msg, MailType type) {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fnctracker@gmail.com", password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("fnctracker@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setText(msg);
            switch (type) {
                case ERROR:
                    message.setSubject("Error occurred");
                    break;
                case SUCCESS:
                    message.setSubject("Adding successful");
                    break;
                case INFORMATION:
                default:
                    message.setSubject("New Mail from Finance Tracker");
                    break;
            }

            this.logger.info("[MailSenderService] sending...");
            Transport.send(message);
            this.logger.info("[MailSenderService] Sent message successfully.");
        } catch (MessagingException e) {
            this.logger.error(e.getMessage());
        }
    }
}
