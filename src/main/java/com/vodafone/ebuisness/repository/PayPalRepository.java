package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.dto.paypal.Token;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.ProductsInDeal;

public interface PayPalRepository {

    String getRecentToken();

    String sendInvoice(String invoiceId);

    String createDraftInvoice(ProductsInDeal productsInDeal, Account account);

    void cancelInvoice(String invoiceId);

}
