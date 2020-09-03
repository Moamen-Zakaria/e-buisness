package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.MailingService;
import com.vodafone.ebuisness.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class MailingServiceImplTest {

    private MailingService mailingService;

    @Autowired
    private JavaMailSender javaMailSender;

    private AuthService authService;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {

        MailingServiceImpl mailingServiceImpl = new MailingServiceImpl();

        javaMailSender = spy(javaMailSender.getClass());
        authService = spy(AuthService.class);
        subscriptionService = spy(SubscriptionService.class);

        mailingServiceImpl.setAuthService(authService);
        mailingServiceImpl.setJavaMailSender(javaMailSender);
        mailingServiceImpl.setSubscriptionService(subscriptionService);

        mailingService = mailingServiceImpl;

    }

    @Test
    void sendRegistrationMail() throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //argument matcher for mocking and verifying javaMailSender api
        ArgumentMatcher<MimeMessage> mimeMessageArgumentMatcher = argument -> {
            try {
                Boolean subjectMatch = argument.getSubject().equals("ebuisness registeration mail");
                Boolean emailMatch = argument.getRecipients(Message.RecipientType.TO)[0].toString().equals("example@abc.com");
                return subjectMatch && emailMatch;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        };

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doAnswer(invocation -> null).when(javaMailSender).send(mimeMessage);
        mailingService.sendRegistrationMail("example@abc.com", null);
        verify(javaMailSender).send(argThat(mimeMessageArgumentMatcher));

    }

    @Test
    void sendInvoiceMail() {
    }

    @Test
    void sendProductNewsToEmail() {
    }

    @Test
    void sendProductNews() {
    }

    @Test
    void sendRegistrationEmail() {
    }

}