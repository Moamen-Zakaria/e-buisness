package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, ObjectId>  {

}


