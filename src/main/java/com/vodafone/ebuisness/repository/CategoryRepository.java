package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
}
