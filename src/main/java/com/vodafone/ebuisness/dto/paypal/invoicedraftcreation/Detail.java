package com.vodafone.ebuisness.dto.paypal.invoicedraftcreation;

public class Detail {

    private String invoice_number;
    private String reference;
    private String invoice_date;
    private String currency_code;
    private String note;
    private String term;
    private String memo;
//    Payment_term Payment_term;

//    public static class Payment_term {
//        private String term_type;
//        private String due_date;
//
//        public String getTerm_type() {
//            return term_type;
//        }
//
//        public void setTerm_type(String term_type) {
//            this.term_type = term_type;
//        }
//
//        public String getDue_date() {
//            return due_date;
//        }
//
//        public void setDue_date(String due_date) {
//            this.due_date = due_date;
//        }
//    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

//    public Detail.Payment_term getPayment_term() {
//        return Payment_term;
//    }
//
//    public void setPayment_term(Detail.Payment_term payment_term) {
//        Payment_term = payment_term;
//    }
}
