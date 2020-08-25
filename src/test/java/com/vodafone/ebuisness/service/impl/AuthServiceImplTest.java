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

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceImplTest {

    @Autowired
    @Qualifier("authServiceImpl")
    private AuthService authService;

    @Autowired
    private AccountRepository accountRepository;

    private ObjectId id;

    @BeforeAll
    void setUp() {
        Account account = new Account();
        account.setUsername("user1");
        account.setEmail("email1@abc.com");
        account.setPassword("password");
        Account account2 = new Account();
        account.setUsername("user2");
        account.setEmail("email2@abc.com");
        account.setPassword("password");
        accountRepository.insert(account);
        accountRepository.insert(account2);
    }

    @Test
    @Order(1)
    void registerAccount() throws EmailAlreadyExistException {

        Date date = new Date(17, 9, 1995);

        Address address = new Address();
        address.setCountry("Some country");
        address.setProvince("some province");

        PersonName personName
                = new PersonName("firstName", "middleName", "lastName");

        Account account = new Account();
        account.setUsername("some Name");
        account.setPassword("password");
        account.setEmail("example@abc.com");
        account.setPersonName(personName);
        account.setAddress(address);
        account.setDateOfBirth(date);
        Boolean isSaved = authService.register(account);

        Assertions.assertEquals(true, isSaved);
        Assertions.assertNotNull(account.getObjectId());

        id = account.getObjectId();

    }

    @Test
    @Order(2)
    void findAccountByEmail() throws EmailDoesNotExistException {
        Account account = authService.findAccountByEmail("example@abc.com");
        Assertions.assertNotNull(account);
        Assertions.assertEquals("example@abc.com", account.getEmail());

    }

    @Test
    @Order(3)
    void loadUserByUsername() {
        var userDetails
                = ((UserDetailsService) authService).loadUserByUsername("example@abc.com");
        Assertions.assertEquals("example@abc.com", userDetails.getUsername());
    }

    @Test
    @Order(4)
    void findAccountByUsername() throws EmailDoesNotExistException {
        Account account = authService.findAccountByUsername("some Name");
        Assertions.assertEquals("some Name", account.getUsername());
    }

    @Test
    @Order(5)
    void findAccountById() throws EmailDoesNotExistException {
        Account account = authService.findAccountById(id.toHexString());
        Assertions.assertNotNull(account);
    }

    @Test
    @Order(6)
    void getAllAccounts() {
        var listOfAccounts = authService.getAllAccounts();
        Assertions.assertNotNull(listOfAccounts);
        Assertions.assertEquals(3 , listOfAccounts.size());
    }

    @Test
    @Order(7)
    void update() throws EmailDoesNotExistException {
        Account account = authService.findAccountById(id.toHexString());
        account.setUsername("Some Username");
        authService.update(account);
        Account newAccount = authService.findAccountById(id.toHexString());
        Assertions.assertEquals("Some Username", newAccount.getUsername());
    }

    @Test
    @Order(8)
    void login() throws LoginFailException {
        Account account = authService.login("example@abc.com", "password");
        Assertions.assertNotNull(account);
        Assertions.assertEquals("example@abc.com", account.getEmail());
    }

    @Test
    @Order(9)
    void loginFail() {
        Assertions.assertThrows(LoginFailException.class,
                () -> authService.login("wrong email", "wrong password"));
    }

    @Test
    @Order(10)
    void grantRole() throws EmailDoesNotExistException {
        Boolean isUpdated = authService.grantRole("example@abc.com", "Role_Test");
        Account account = authService.findAccountById(id.toHexString());
        Assertions.assertEquals(true, isUpdated);
        Assertions.assertNotNull(account.getRoles());
        Assertions.assertEquals(true, account.getRoles().contains("Role_Test"));
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.grantRole("wrong email", "Role_Test"));
    }

    @Test
    @Order(11)
    void hasRole() throws EmailDoesNotExistException {
        Boolean hasRole = authService.hasRole("example@abc.com", "Role_Test");
        Account account = authService.findAccountById(id.toHexString());
        Assertions.assertTrue(hasRole);
        Assertions.assertNotNull(account.getRoles());
        Assertions.assertTrue(account.getRoles().contains("Role_Test"));
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.hasRole("wrong email", "Role_Test"));
    }

    @Test
    @Order(12)
    void revokeRole() throws EmailDoesNotExistException {
        Boolean isRevoked = authService.revokeRole("example@abc.com", "Role_Test");
        Account account = authService.findAccountById(id.toHexString());
        Assertions.assertTrue(isRevoked);
        if (account.getRoles() != null) {
            Assertions.assertFalse(account.getRoles().contains("Role_Test"));
        }
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.revokeRole("wrong email", "Role_Test"));
    }

    @Test
    @Order(13)
    void deleteAccount() throws EmailDoesNotExistException {
        authService.deleteAccount("example@abc.com");
        Assertions.assertThrows(EmailDoesNotExistException.class,
                () -> authService.findAccountById(id.toHexString()));
    }

}