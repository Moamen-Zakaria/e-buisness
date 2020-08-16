package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.configuration.PropertiesMapping;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.MailingService;
import com.vodafone.ebuisness.service.SubscriptionService;
import com.vodafone.ebuisness.util.PropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailingServiceImpl implements MailingService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    PropertiesLoader propertiesLoader;

    @Autowired
    AuthService authService;

    @Autowired
    SubscriptionService subscriptionService;

    @Override
    public void sendRegistrationMail(String email, String name) throws MessagingException {

        if (name == null || name.trim().equals("")) {
            name = "Sir/Madame";
        }

        Properties properties
                = propertiesLoader.loadProperties(PropertiesMapping.MARKETING_MAIL);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setFrom(properties.getProperty("mail.email"));
        mimeMessage.setSubject("ebuisness registeration mail");
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        mimeMessage.setText("Dear " + name + ",\n\nYour email has been successfully registered!\n\n" +
                "Thanks\nEBuisness team");
        javaMailSender.send(mimeMessage);

    }

    @Override
    public void sendInvoiceMail(Account account, String link) throws MessagingException {

        if (account == null ) {
            throw new IllegalArgumentException();
        }

        if (link == null || link.trim().equals("")) {
            throw new IllegalArgumentException();
        }

        Properties properties
                = propertiesLoader.loadProperties(PropertiesMapping.MARKETING_MAIL);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setFrom(properties.getProperty("mail.email"));
        mimeMessage.setSubject("invoice mail");
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(account.getEmail()));
        mimeMessage.setText("Dear " + account.getPersonName() + ",\n\nThanks for buying our product, " +
                "please follow this link , " + link +", for your bill details.\n\n" +
                "Sincerely\nEBuisness team");
        javaMailSender.send(mimeMessage);

    }

    @Override
    public void sendProductNewsToEmail(String email, Product product, String name) throws MessagingException {

        if (name == null || name.trim().equals("")) {
            name = "Sir/Madame";
        }
        Properties properties
                = propertiesLoader.loadProperties(PropertiesMapping.MARKETING_MAIL);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setFrom(properties.getProperty("mail.email"));
        mimeMessage.setSubject("ebuisness registeration mail");
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        mimeMessage.setText("Dear " + name + ",\n\nWe are pleased to inform you that we got a new " +
                "product in the market that, we think, might interest you.\nOur new product is a " + product.getName()
                + ", hurry and check it up before it runs out of stock.\n\n" +
                "Thanks\nEBuisness team");
        javaMailSender.send(mimeMessage);

    }

    @Override
    public void sendProductNews(Product product)
            throws NoSuchProductException {

        var setOfMails
                = subscriptionService.getEmailsRelatedToProduct(product.getObjectId().toHexString());

        setOfMails.stream().parallel().map(email -> {

            Account account;
            try {
                account = authService.findAccountByEmail(email);
            } catch (EmailDoesNotExistException e) {
                account = new Account();
                account.setEmail(email);
                account.setPersonName(new PersonName("", "", ""));
                return account;
            }
            return account;
        }).forEach(account -> {

            try {
                sendProductNewsToEmail(account.getEmail(), product, account.getPersonName().toString());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public void sendRegistrationEmail(Account account) throws MessagingException {

        sendRegistrationMail(account.getEmail(), account.getPersonName().toString());

    }


}
