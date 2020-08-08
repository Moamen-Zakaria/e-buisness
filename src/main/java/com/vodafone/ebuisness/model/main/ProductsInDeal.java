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

    @DBRef
    private Payment payment;

    public ProductsInDeal() {
    }

    public ProductsInDeal(List<ProductInCart> productInCartList, Account account, Payment payment) {
        this.productInCartList = productInCartList;
        this.account = account;
        this.payment = payment;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
