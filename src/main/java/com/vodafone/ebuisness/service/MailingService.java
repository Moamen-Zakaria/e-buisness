package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.Product;

import javax.mail.MessagingException;

public interface MailingService {

    void sendRegistrationMail(String email, String name) throws MessagingException;

    void sendInvoiceMail(Account account, String link) throws MessagingException;

    void sendProductNewsToEmail(String email, Product product, String name) throws MessagingException;

    void sendProductNews(Product product) throws NoSuchProductException;

    void sendRegistrationEmail(Account account) throws MessagingException;

}
