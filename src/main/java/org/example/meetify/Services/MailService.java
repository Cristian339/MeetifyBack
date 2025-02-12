package org.example.meetify.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.example.meetify.DTO.Mail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMail(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(mail.getEmail());
        helper.setSubject(mail.getContent());
        helper.setText(mail.getSubject(), true);

        mailSender.send(mimeMessage);
    }
}