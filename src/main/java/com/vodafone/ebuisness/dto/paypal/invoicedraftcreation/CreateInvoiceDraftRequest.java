package com.vodafone.ebuisness.dto.paypal.invoicedraftcreation;

import java.util.ArrayList;

public class CreateInvoiceDraftRequest {

    //    Invoicer invoicer;
    private Detail detail;
    private ArrayList<PrimaryRecipient> primary_recipients;
    private ArrayList<Item> items;
//    Configuration ConfigurationObject;
//    Amount AmountObject;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public ArrayList<PrimaryRecipient> getPrimary_recipients() {
        return primary_recipients;
    }

    public void setPrimary_recipients(ArrayList<PrimaryRecipient> primary_recipients) {
        this.primary_recipients = primary_recipients;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}

class Amount {
    Breakdown BreakdownObject;
}

class Breakdown {
    Custom CustomObject;
    Shipping ShippingObject;
    Discount DiscountObject;

}

class Discount {
    Invoice_discount Invoice_discountObject;
}

class Invoice_discount {
    private String percent;

}

class Shipping {
    Amount AmountObject;
    Tax TaxObject;

}

class Tax {
    private String name;
    private String percent;

}

class AmountValue {
    private String currency_code;
    private String value;

}

class Custom {
    private String label;
    Amount AmountObject;


}


class Configuration {
    Partial_payment Partial_paymentObject;
    private boolean allow_tip;
    private boolean tax_calculated_after_discount;
    private boolean tax_inclusive;
    private String template_id;


}

class Partial_payment {
    private boolean allow_partial_payment;
    Minimum_amount_due Minimum_amount_dueObject;


}

class Minimum_amount_due {
    private String currency_code;
    private String value;


}

class Invoicer {
    Name NameObject;
    Address AddressObject;
    private String email_address;
    ArrayList<Object> phones = new ArrayList<Object>();
    private String website;
    private String tax_id;
    private String logo_url;
    private String additional_notes;


}

class Address {
    private String address_line_1;
    private String address_line_2;
    private String admin_area_2;
    private String admin_area_1;
    private String postal_code;
    private String country_code;


}

class Name {
    private String given_name;
    private String surname;


}



