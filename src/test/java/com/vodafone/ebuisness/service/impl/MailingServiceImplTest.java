package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.MailingService;
import com.vodafone.ebuisness.service.SubscriptionService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.*;

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
    void sendInvoiceMailInHappyScenario() throws MessagingException {

        Account account = getAccount();
        ArgumentMatcher<MimeMessage> mimeMessageArgumentMatcher = argument -> {
            try {
                Boolean subjectMatch = argument.getSubject().equals("invoice mail");
                Boolean emailMatch = false;
                if (argument.getRecipients(Message.RecipientType.TO).length >= 1) {
                    emailMatch = argument.getRecipients(Message.RecipientType.TO)[0].toString().equals(account.getEmail());
                }
                return subjectMatch && emailMatch;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        };

        doAnswer(invocation -> null).when(javaMailSender).send(any(MimeMessage.class));
        mailingService.sendInvoiceMail(account, "www.link.com");
        verify(javaMailSender).send(argThat(mimeMessageArgumentMatcher));

    }

    @Test
    void sendInvoiceMailIfNullAccountUsed() throws MessagingException {

        Account account = getAccount();
        ArgumentMatcher<MimeMessage> mimeMessageArgumentMatcher = argument -> {
            try {
                Boolean subjectMatch = argument.getSubject().equals("invoice mail");
                Boolean emailMatch = false;
                if (argument.getRecipients(Message.RecipientType.TO).length >= 1) {
                    emailMatch = argument.getRecipients(Message.RecipientType.TO)[0].toString().equals(account.getEmail());
                }
                return subjectMatch && emailMatch;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        };

        doAnswer(invocation -> null).when(javaMailSender).send(any(MimeMessage.class));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                mailingService.sendInvoiceMail(null, "www.link.com"));

    }

    @Test
    void sendInvoiceMailIfNullLinkUsed() throws MessagingException {

        Account account = getAccount();
        ArgumentMatcher<MimeMessage> mimeMessageArgumentMatcher = argument -> {
            try {
                Boolean subjectMatch = argument.getSubject().equals("invoice mail");
                Boolean emailMatch = false;
                if (argument.getRecipients(Message.RecipientType.TO).length >= 1) {
                    emailMatch = argument.getRecipients(Message.RecipientType.TO)[0].toString().equals(account.getEmail());
                }
                return subjectMatch && emailMatch;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        };

        doAnswer(invocation -> null).when(javaMailSender).send(any(MimeMessage.class));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                mailingService.sendInvoiceMail(account, null));

    }

    @Test
    void sendProductNewsToEmail() throws MessagingException {

        doAnswer(invocation -> null).when(javaMailSender).send(any(MimeMessage.class));
        Assertions.assertDoesNotThrow(() ->
                mailingService.sendProductNewsToEmail("example@abc.com", getProduct(), ""));

    }

    @Test
    void sendProductNews() throws NoSuchProductException, EmailDoesNotExistException {

        Product product = getProduct();
        product.setCategories(new ArrayList<>());
        Set subscribersSet = new HashSet<String>();
        subscribersSet.add("example@abc.com");
        Category category = new Category("cosmetics", "", subscribersSet);
        product.getCategories().add(category);

        when(subscriptionService.getEmailsRelatedToProduct(product.getObjectId().toHexString()))
                .thenReturn(new HashSet<>(Arrays.asList("example@abc.com")));
        when(authService.findAccountByEmail("example@abc.com")).thenThrow(EmailDoesNotExistException.class);
        doAnswer(invocation -> null).when(javaMailSender).send(any(MimeMessage.class));
        mailingService.sendProductNews(product);

    }

    @Test
    void sendRegistrationEmail() throws MessagingException {
        Account account = getAccount();

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
        mailingService.sendRegistrationEmail(account);
        verify(javaMailSender).send(argThat(mimeMessageArgumentMatcher));

    }

    private Account getAccount() {

        Date date = new Date(17, 9, 1995);

        Address address = new Address();
        address.setCountry("Some country");
        address.setProvince("some province");

        PersonName personName
                = new PersonName("firstName", "middleName", "lastName");

        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setUsername("some Name");
        account.setPassword("password");
        account.setEmail("example@abc.com");
        account.setPersonName(personName);
        account.setAddress(address);
        account.setDateOfBirth(date);

        return account;
    }

    private Product getProduct() {

        Product product = new Product();
        product.setQuantity(5);
        product.setPrice(1000.0);
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");

        return product;
    }

}