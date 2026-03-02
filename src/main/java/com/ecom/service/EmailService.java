package com.ecom.service;

import com.ecom.exception.EmailFailureException;
import com.ecom.entity.User;
import com.ecom.entity.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String url;

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private SimpleMailMessage makeMailMessage(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }

    public void sendEmail(VerificationToken verificationToken) throws EmailFailureException{
        SimpleMailMessage simpleMailMessage = makeMailMessage();
        simpleMailMessage.setTo(verificationToken.getUser().getEmail());
        simpleMailMessage.setSubject("Verify your email to activate your account");
        simpleMailMessage.setText("Please follow the link below to verify your email to active your account :\n "+
                url + "/auth/verify?token="+verificationToken.getToken());

        try {
            System.out.println("FROM: " + fromAddress);
            System.out.println("URL: " + url);
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new EmailFailureException();
        }

    }

    public void sendPasswordResetMail(User user, String token) throws EmailFailureException{
        SimpleMailMessage simpleMailMessage = makeMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Your Password Reset Link");
        simpleMailMessage.setText("You requested a password reset on our website please find the link below" +
                "to be able to reset your password :\n "+
                url + "/auth/reset?token="+token);

        try {
            System.out.println("FROM: " + fromAddress);
            System.out.println("URL: " + url);
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new EmailFailureException();
        }
    }
}
