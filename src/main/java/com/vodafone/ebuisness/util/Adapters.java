package com.vodafone.ebuisness.util;

import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.Item;
import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.PrimaryRecipient;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import com.vodafone.ebuisness.model.main.Account;

public class Adapters {

    public static Item convertProductToItem(ProductInCart productInCart) {

        Item item = new Item();
//        item.setId(productInCart.getProduct().getObjectId().toHexString());
        item.setName(productInCart.getProduct().getName());
        item.setDescription(productInCart.getProduct().getDescription());
        item.setQuantity(productInCart.getRequiredQuantity().toString());

        var unitAmount = new Item.UnitAmount();
        unitAmount.setValue(productInCart.getProduct().getPrice().toString());
        item.setUnit_amount(unitAmount);

        return item;
    }

    public static PrimaryRecipient convertAccountToPrimaryRecipient(Account account) {

        var primaryRecipient = new PrimaryRecipient();
        var billingInfo = new PrimaryRecipient.BillingInfo();
        billingInfo.setEmail_address(account.getEmail());

        if (account.getAddress() == null) {
            var address = new PrimaryRecipient.BillingInfo.Address();
            address.setPostal_code(account.getAddress().getPostalCode().toString());
            address.setCountry_code("EG");
            String addressLine = "";
            if (account.getAddress().getBlockNo() != null)
                addressLine += account.getAddress().getBlockNo() + " ";
            if (account.getAddress().getStreet() != null)
                addressLine += "St." + account.getAddress().getStreet() + " ";
            if (account.getAddress().getCity() != null)
                addressLine += account.getAddress().getCity() + " City ";
            if (account.getAddress().getDistrict() != null)
                addressLine += account.getAddress().getDistrict() + " District ";
            if (account.getAddress().getProvince() != null)
                addressLine += "- " + account.getAddress().getProvince() + " ";
            if (account.getAddress().getCountry() != null)
                addressLine += "- " + account.getAddress().getCountry() + " ";
            address.setAddress_line_1(addressLine);
            billingInfo.setAddress(address);
        }

        primaryRecipient.setBilling_info(billingInfo);
        return primaryRecipient;
    }

}
