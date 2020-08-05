package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.Payment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, ObjectId> {
}
