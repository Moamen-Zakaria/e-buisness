package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.PaymentMean;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentMeanRepository extends MongoRepository<PaymentMean, String> {

}
