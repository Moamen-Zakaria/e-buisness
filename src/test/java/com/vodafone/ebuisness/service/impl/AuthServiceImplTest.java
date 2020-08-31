package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.LoginFailException;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.repository.AccountRepository;
import com.vodafone.ebuisness.service.AuthService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceImplTest {

    public AuthServiceImplTest() {
        AuthServiceImpl authServiceImpl = new AuthServiceImpl();
        accountRepository = spy(AccountRepository.class);
        authServiceImpl.setAccountRepository(accountRepository);
        authService = authServiceImpl;
    }

    private AuthService authService;
    private AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() throws EmailAlreadyExistException {
        reset(accountRepository);
    }


    @Test
    void registerAccount() throws EmailAlreadyExistException {

        Date date = new Date(17, 9, 1995);

        Address address = new Address();
        address.setCountry("Some country");
        address.setProvince("some province");

        PersonName personName
                = new PersonName("firstName", "middleName", "lastName");

        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setUsername("some Name");
        account.setPassword("password");
        account.setEmail("example@abc.com");
        account.setPersonName(personName);
        account.setAddress(address);
        account.setDateOfBirth(date);
        when(accountRepository.save(account)).thenReturn(account);
        Boolean isSaved = authService.register(account);

        Assertions.assertEquals(true, isSaved);
        Assertions.assertNotNull(account.getObjectId());

        verify(accountRepository).save(account);

    }

    @Test
    void findAccountByEmail() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setEmail("example@abc.com");
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        account = authService.findAccountByEmail("example@abc.com");
        Assertions.assertNotNull(account);
        Assertions.assertEquals("example@abc.com", account.getEmail());
        verify(accountRepository).findByEmail("example@abc.com");
    }

    @Test
    void loadUserByUsername() {
        Account account = new Account();
        account.setEmail("example@abc.com");
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        var userDetails
                = ((UserDetailsService) authService).loadUserByUsername("example@abc.com");
        Assertions.assertEquals("example@abc.com", userDetails.getUsername());
        verify(accountRepository).findByEmail("example@abc.com");

    }

    @Test
    void findAccountByUsername() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setUsername("some Name");
        when(accountRepository.findByUsername("some Name")).thenReturn(account);
        account = authService.findAccountByUsername("some Name");
        Assertions.assertEquals("some Name", account.getUsername());
        verify(accountRepository).findByUsername("some Name");
    }

    @Test
    void findAccountById() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setObjectId(new ObjectId());
        when(accountRepository.findById(account.getObjectId())).thenReturn(Optional.of(account));
        account = authService.findAccountById(account.getObjectId().toHexString());
        Assertions.assertNotNull(account);
        verify(accountRepository).findById(account.getObjectId());
    }

    @Test
    void getAllAccounts() {
        var listOfAccounts = new ArrayList<Account>();
        listOfAccounts.add(new Account());
        listOfAccounts.add(new Account());
        listOfAccounts.add(new Account());
        when(accountRepository.findAll()).thenReturn(listOfAccounts);
        var newListOfAccounts = authService.getAllAccounts();
        Assertions.assertNotNull(newListOfAccounts);
        Assertions.assertEquals(3, newListOfAccounts.size());
    }

    @Test
    void update() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setEmail("example@abc.com");
        when(accountRepository.findById(account.getObjectId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        account = authService.findAccountById(account.getObjectId().toHexString());
        account.setUsername("Some Username");
        authService.update(account);
        Account newAccount = authService.findAccountById(account.getObjectId().toHexString());
        Assertions.assertEquals("Some Username", newAccount.getUsername());
    }

    @Test
    void login() throws LoginFailException {
        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setEmail("example@abc.com");
        when(accountRepository.findByEmailAndPassword("example@abc.com" , "password" )).thenReturn(account);
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        account = authService.login("example@abc.com", "password");
        Assertions.assertNotNull(account);
        Assertions.assertEquals("example@abc.com", account.getEmail());
    }

    @Test
    void loginFail() {
        Assertions.assertThrows(LoginFailException.class,
                () -> authService.login("wrong email", "wrong password"));
    }

    @Test
    void grantRole() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setEmail("example@abc.com");
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(account.getObjectId())).thenReturn(Optional.of(account));
        Boolean isUpdated = authService.grantRole("example@abc.com", "Role_Test");
        account = authService.findAccountById(account.getObjectId().toHexString());
        Assertions.assertEquals(true, isUpdated);
        Assertions.assertNotNull(account.getRoles());
        Assertions.assertEquals(true, account.getRoles().contains("Role_Test"));
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.grantRole("wrong email", "Role_Test"));
    }

    @Test
    void hasRole() throws EmailDoesNotExistException {
        Account account = new Account();
        var setOfRoles = new HashSet<String>();
        setOfRoles.add("Role_Test");
        account.setObjectId(new ObjectId());
        account.setEmail("example@abc.com");
        account.setRoles(setOfRoles);
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        when(accountRepository.findById(account.getObjectId())).thenReturn(Optional.of(account));
        Boolean hasRole = authService.hasRole("example@abc.com", "Role_Test");
        account = authService.findAccountById(account.getObjectId().toHexString());
        Assertions.assertTrue(hasRole);
        Assertions.assertNotNull(account.getRoles());
        Assertions.assertTrue(account.getRoles().contains("Role_Test"));
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.hasRole("wrong email", "Role_Test"));
    }

    @Test
    void revokeRole() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setEmail("example@abc.com");
        account.setObjectId(new ObjectId());
        var setOfRoles = new HashSet<String>();
        setOfRoles.add("Role_Test");
        account.setRoles(setOfRoles);
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        when(accountRepository.findById(account.getObjectId())).thenReturn(Optional.of(account));
        Boolean isRevoked = authService.revokeRole("example@abc.com", "Role_Test");
        account = authService.findAccountById(account.getObjectId().toHexString());
        Assertions.assertTrue(isRevoked);
        if (account.getRoles() != null) {
            Assertions.assertFalse(account.getRoles().contains("Role_Test"));
        }
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.revokeRole("wrong email", "Role_Test"));
    }

    @Test
    void deleteAccount() throws EmailDoesNotExistException {
        Account account = new Account();
        account.setEmail("example@abc.com");
        account.setObjectId(new ObjectId());
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(account);
        authService.deleteAccount("example@abc.com");
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.findAccountById(account.getObjectId().toHexString()));
    }

}