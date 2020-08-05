package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.ProductsInDeal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductsInDealRepository extends MongoRepository<ProductsInDeal, ObjectId> {
}
