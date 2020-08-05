package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, ObjectId> {
}
