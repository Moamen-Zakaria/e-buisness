package com.vodafone.ebuisness.model.main;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.vodafone.ebuisness.model.auxiliary.ProductInCart;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products_in_deal")
public class ProductsInDeal {

    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private ObjectId objectId;

    private List<ProductInCart> productInCartList;

    @DBRef
    private Account account;

    private Invoice invoice;

    public ProductsInDeal() {
    }

    public ProductsInDeal(List<ProductInCart> productInCartList, Account account, Invoice invoice) {
        this.productInCartList = productInCartList;
        this.account = account;
        this.invoice = invoice;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public List<ProductInCart> getProductInCartList() {
        return productInCartList;
    }

    public void setProductInCartList(List<ProductInCart> productInCartList) {
        this.productInCartList = productInCartList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
