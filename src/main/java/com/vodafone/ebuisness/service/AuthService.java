package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.LoginFailException;
import com.vodafone.ebuisness.model.main.Account;

import java.util.List;

public interface AuthService {

    Boolean register(Account account) throws EmailAlreadyExistException;

    Account update(Account account) throws EmailDoesNotExistException;

    void deleteAccount(String id) throws EmailDoesNotExistException;

    Account findAccountById(String id) throws EmailDoesNotExistException;

    Account findAccountByEmail(String email) throws EmailDoesNotExistException;

    Account findAccountByUsername(String username) throws EmailDoesNotExistException;

    List<Account> getAllAccounts();

    Account login(String usernameOrEmail, String password) throws LoginFailException;

    Boolean grantRole(String email, String role) throws EmailDoesNotExistException;

    Boolean revokeRole(String email, String role) throws EmailDoesNotExistException;

    Boolean hasRole(String email, String role) throws EmailDoesNotExistException;

}
