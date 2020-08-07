package com.vodafone.ebuisness.repository;

import com.vodafone.ebuisness.model.main.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account, ObjectId> {

    public Account findByEmail(String email);
    public Account findByUsername(String email);
    public Account findByEmailAndPassword(String email , String password);
    public Account findByUsernameAndPassword(String email , String password);
    public void deleteByEmail(String email);

}
