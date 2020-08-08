package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.ProductsInDeal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductsInDealRepository extends MongoRepository<ProductsInDeal, ObjectId> {

    @Query(" { $and : [ {'account' :{'$ref' : 'account' , '$id' : ?0 }} , { 'payment' : null } ]}")
    ProductsInDeal findByAccount_IdAndPaymentIsNull(Object objectId);

}
