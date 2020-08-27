package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.exception.ConnectionErrorException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.ProductsInDeal;

public interface PayPalService {

    String sendInvoice(String invoiceId) throws ConnectionErrorException;

    String createDraftInvoice(ProductsInDeal productsInDeal, Account account) throws ConnectionErrorException;

    void cancelInvoice(String invoiceId) throws ConnectionErrorException;

}
