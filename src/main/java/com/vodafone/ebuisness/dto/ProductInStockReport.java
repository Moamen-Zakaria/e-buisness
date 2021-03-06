package com.vodafone.ebuisness.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

public class ProductInStockReport {

    @JsonProperty("object_id")
    private ObjectId objectId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("available_quantity")
    private Integer availableQuantity;

    @JsonProperty("required_quantity")
    private Integer requiredQuantity;

    private Boolean isInStock;

    public ProductInStockReport(String productName, Integer availableQuantity, Integer requiredQuantity) {

        if (productName == null || availableQuantity == null || requiredQuantity == null) {
            throw new IllegalArgumentException("ProductInStockReport class creation" +
                    "error: Constructor parameters can't be null");
        }
        if (availableQuantity < 0 || requiredQuantity < 1) {
            throw new IllegalArgumentException("ProductInStockReport class creation error: " +
                    "Constructor parameters values are not valid");
        }

        this.productName = productName;
        this.availableQuantity = availableQuantity;
        this.requiredQuantity = requiredQuantity;
        setStockStatus();
    }

    private void setStockStatus() {
        isInStock = availableQuantity >= requiredQuantity;
    }

    @JsonProperty("in_stock")
    public Boolean getInStock() {
        return isInStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
        setStockStatus();
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
        setStockStatus();
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }
}
