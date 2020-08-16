package com.vodafone.ebuisness.dto.paypal.invoicedraftcreation;

public class Item {

    private String id;
    private String name;
    private String description;
    private String quantity;
    private UnitAmount unit_amount;

    public static class UnitAmount {

        private String currency_code = "USD";
        private String value;

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public UnitAmount getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(UnitAmount unit_amount) {
        this.unit_amount = unit_amount;
    }
}
