package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.LoginFailException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.repository.AccountRepository;
import com.vodafone.ebuisness.service.AuthService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Boolean register(Account account) throws EmailAlreadyExistException {

        var existingAccount = accountRepository.findByEmail(account.getEmail());
        if (existingAccount == null) {
            account = accountRepository.save(account);
        } else {
            throw new EmailAlreadyExistException();
        }
        return account.getObjectId() != null;

    }


    @Override
    public Account update(Account account) throws EmailDoesNotExistException {

        var existingAccount = accountRepository.findByEmail(account.getEmail());
        if (existingAccount != null) {
            account = accountRepository.save(account);
        } else {
            throw new EmailDoesNotExistException();
        }
        return account;
    }

    @Override
    public void deleteAccount(String email) throws EmailDoesNotExistException {
        var existingAccount = accountRepository.findByEmail(email);
        if (existingAccount != null) {

            accountRepository.deleteByEmail(email);

        } else {

            throw new EmailDoesNotExistException();
        }

    }

    @Override
    public Account findAccountById(String id) throws EmailDoesNotExistException {

        var existingAccountOptional = accountRepository.findById(new ObjectId(id));
        Account account;

        try {

            account = existingAccountOptional.get();

        } catch (NoSuchElementException ex) {

            throw new EmailDoesNotExistException();

        }

        return account;
    }

    @Override
    public Account findAccountByEmail(String email) throws EmailDoesNotExistException {

        var existingAccount = accountRepository.findByEmail(email);
        if (existingAccount == null) {

            throw new EmailDoesNotExistException();

        }
        return existingAccount;
    }

    @Override
    public Account findAccountByUsername(String username) throws EmailDoesNotExistException {

        var existingAccount = accountRepository.findByUsername(username);
        if (existingAccount == null) {

            throw new EmailDoesNotExistException();

        }
        return existingAccount;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account login(String usernameOrEmail, String password) throws LoginFailException {

        Account account =
                accountRepository.findByEmailAndPassword(usernameOrEmail, password);

        if (account == null) {

            account = accountRepository.findByUsernameAndPassword(usernameOrEmail, password);

        }

        if (account == null) {
            throw new LoginFailException();
        }

        return account;
    }

    @Override
    public Boolean grantRole(String email, String role) throws EmailDoesNotExistException {

        var account = findAccountByEmail(email);
        var roles = account.getRoles();

        if (roles == null) {

            roles = new HashSet<>();
            account.setRoles(roles);

        }

        Boolean isGranted = roles.add(role);

        if (isGranted) {
            update(account);
        }
        return isGranted;

    }

    @Override
    public Boolean revokeRole(String email, String role) throws EmailDoesNotExistException {

        var account = findAccountByEmail(email);
        var roles = account.getRoles();
        Boolean isGranted = false;

        if (roles != null) {

            account.setRoles(roles);
            isGranted = roles.remove(role);
            if (isGranted) {
                update(account);
            }

        }

        return isGranted;

    }

    @Override
    public Boolean hasRole(String email, String role) throws EmailDoesNotExistException {

        var account = findAccountByEmail(email);

        if (account == null) {

            return false;

        } else {

            return account.getRoles().contains(role);

        }

    }

}
