package org.example.rgybackend.Service;

import jakarta.mail.MessagingException;

public interface SjtuMailService {
    void sendSimpleMail(String to, String subject, String text);

    void sendHtmlMail(String to, String subject, String html) throws MessagingException;
}
