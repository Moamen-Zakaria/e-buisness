package com.vodafone.ebuisness.dto.paypal.invoicedraftcreation;

import java.util.List;

public class PrimaryRecipient {

    private BillingInfo billing_info;
//    private ShippingInfo shipping_info;

    public static class BillingInfo {

        private Address address;
        private String email_address;
        private List<Phone> phones;

        public static class Phone {

            private String country_code;
            private String national_number;
            private String phone_type;

            public String getCountry_code() {
                return country_code;
            }

            public void setCountry_code(String country_code) {
                this.country_code = country_code;
            }

            public String getNational_number() {
                return national_number;
            }

            public void setNational_number(String national_number) {
                this.national_number = national_number;
            }

            public String getPhone_type() {
                return phone_type;
            }

            public void setPhone_type(String phone_type) {
                this.phone_type = phone_type;
            }
        }

        public static class Address {

            private String address_line_1;
            private String admin_area_2;
            private String admin_area_1;
            private String postal_code;
            private String country_code;

            public String getAddress_line_1() {
                return address_line_1;
            }

            public void setAddress_line_1(String address_line_1) {
                this.address_line_1 = address_line_1;
            }

            public String getAdmin_area_2() {
                return admin_area_2;
            }

            public void setAdmin_area_2(String admin_area_2) {
                this.admin_area_2 = admin_area_2;
            }

            public String getAdmin_area_1() {
                return admin_area_1;
            }

            public void setAdmin_area_1(String admin_area_1) {
                this.admin_area_1 = admin_area_1;
            }

            public String getPostal_code() {
                return postal_code;
            }

            public void setPostal_code(String postal_code) {
                this.postal_code = postal_code;
            }

            public String getCountry_code() {
                return country_code;
            }

            public void setCountry_code(String country_code) {
                this.country_code = country_code;
            }
        }

        public static class Name {
            private String given_name;
            private String surname;

            public String getGiven_name() {
                return given_name;
            }

            public void setGiven_name(String given_name) {
                this.given_name = given_name;
            }

            public String getSurname() {
                return surname;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public String getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String email_address) {
            this.email_address = email_address;
        }

        public List<Phone> getPhones() {
            return phones;
        }

        public void setPhones(List<Phone> phones) {
            this.phones = phones;
        }
    }

    public static class ShippingInfo {

        private Name name;

        public static class Name {
            private String given_name;
            private String surname;

            public String getGiven_name() {
                return given_name;
            }

            public void setGiven_name(String given_name) {
                this.given_name = given_name;
            }

            public String getSurname() {
                return surname;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }
        }

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }
    }

    public BillingInfo getBilling_info() {
        return billing_info;
    }

    public void setBilling_info(BillingInfo billing_info) {
        this.billing_info = billing_info;
    }

//    public ShippingInfo getShipping_info() {
//        return shipping_info;
//    }
//
//    public void setShipping_info(ShippingInfo shipping_info) {
//        this.shipping_info = shipping_info;
//    }
}
