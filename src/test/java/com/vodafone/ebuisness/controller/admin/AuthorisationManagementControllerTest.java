package com.vodafone.ebuisness.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.ebuisness.configuration.AccountRole;
import com.vodafone.ebuisness.dto.security.LoginResponse;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorisationManagementControllerTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() throws Exception {

        when(accountRepository.findByEmail("example@abc.com")).thenReturn(getAccount());
        when(accountRepository.findByEmailAndPassword("example@abc.com", "password"))
                .thenReturn(getAccount());
        var response = mockMvc.perform(post("/auth/login")
                .param("email", "example@abc.com")
                .param("password", "password")).andReturn().getResponse();

        loginResponse = new ObjectMapper().readValue(response.getContentAsString(), LoginResponse.class);

    }

    @Test
    void grantRole() throws Exception {

        Account accountOfSomeEmail = getAccount();
        accountOfSomeEmail.setEmail("someEmail@abc.com");
        when(accountRepository.findByEmail("someEmail@abc.com")).thenReturn(accountOfSomeEmail);
        mockMvc.perform(put("/admin/auth/grant").headers(getAuthorisedHeaders())
                .param("email", "someEmail@abc.com")
                .param("role", AccountRole.ROLE_ADMIN))
                .andExpect(status().isNoContent());

    }

    @Test
    void revokeRole() throws Exception {

        Account accountOfSomeEmail = getAccount();
        accountOfSomeEmail.setEmail("someEmail@abc.com");
        accountOfSomeEmail.setRoles(new HashSet<>(Arrays.asList(AccountRole.ROLE_ADMIN)));
        when(accountRepository.findByEmail("someEmail@abc.com")).thenReturn(accountOfSomeEmail);
        mockMvc.perform(put("/admin/auth/revoke").headers(getAuthorisedHeaders())
                .param("email", "someEmail@abc.com")
                .param("role", AccountRole.ROLE_ADMIN))
                .andExpect(status().isNoContent());

    }

    @Test
    void hasRole() throws Exception {

        Account accountOfSomeEmail = getAccount();
        accountOfSomeEmail.setEmail("someEmail@abc.com");
        accountOfSomeEmail.setRoles(new HashSet<>(Arrays.asList(AccountRole.ROLE_ADMIN)));
        when(accountRepository.findByEmail("someEmail@abc.com")).thenReturn(accountOfSomeEmail);

        mockMvc.perform(get("/admin/auth/hasRole/someEmail@abc.com/" + AccountRole.ROLE_ADMIN)
                .headers(getAuthorisedHeaders()))
                .andExpect(content().string("true"));

    }

    @Test
    void getAllAccounts() throws Exception {

        Account account = getAccount();
        var listOfAccounts = new ArrayList<>(Arrays.asList(account, account, account));
        when(accountRepository.findAll()).thenReturn(listOfAccounts);
        mockMvc.perform(get("/admin/auth/accounts")
                .headers(getAuthorisedHeaders()))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    void getAccountById() throws Exception {

        Account account = getAccount();
        account.setObjectId(new ObjectId());
        when(accountRepository.findByEmail("example@abc.com")).thenReturn(getAccount());
        when(accountRepository.findByEmailAndPassword("example@abc.com", "password"))
                .thenReturn(getAccount());
        when(accountRepository.findById(account.getObjectId())).thenReturn(Optional.of(account));
        mockMvc.perform(get("/admin/auth/get/id/" + account.getObjectId().toHexString())
                .headers(getAuthorisedHeaders()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.objectId").value(account.getObjectId().toHexString()));

    }

    @Test
    void getAccountByUsername() throws Exception {
        Account account = getAccount();
        when(accountRepository.findByUsername(account.getUsername())).thenReturn(account);
        mockMvc.perform(get("/admin/auth/get/username/" + account.getUsername())
                .headers(getAuthorisedHeaders()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(account.getUsername()));
    }

    @Test
    void getAccountByEmail() throws Exception {
        Account account = getAccount();
        when(accountRepository.findByEmail(account.getUsername())).thenReturn(account);
        mockMvc.perform(get("/admin/auth/get/email/" + account.getEmail())
                .headers(getAuthorisedHeaders()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").value(account.getEmail()));
    }

    @Test
    void updateAccount() throws Exception {

        Account account = getAccount();
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        mockMvc.perform(put("/admin/auth/update")
                .headers(getAuthorisedHeaders())
                .param("dateOfBirth.year", "1995")
                .param("dateOfBirth.month", "9")
                .param("dateOfBirth.day", "17")
                .param("email", "example@abc.com")
                .param("username", "navras")
                .param("password", "password")
                .param("personName.firstName", "Moamen")
                .param("personName.middleName", "Mohammed")
                .param("personName.lastName", "Mortada")
                .param("address.province", "Luten")
                .param("address.country", "UK")
                .param("account_id", account.getObjectId().toHexString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.objectId").value(account.getObjectId().toHexString()));
    }

    @Test
    void deleteAccount() throws Exception {

        Account someEmailAccount = getAccount();
        someEmailAccount.setEmail("someEmail@abc.com");
        when(accountRepository.findByEmail("someEmail@abc.com")).thenReturn(someEmailAccount);
        mockMvc.perform(delete("/admin/auth/delete")
                .headers(getAuthorisedHeaders())
                .param("email" , "someEmail@abc.com"))
                .andExpect(status().is2xxSuccessful());

    }

    private Account getAccount() {

        Date date = new Date(17, 9, 1995);

        Address address = new Address();
        address.setCountry("Some country");
        address.setProvince("some province");

        PersonName personName
                = new PersonName("firstName", "middleName", "lastName");

        Account account = new Account();
        account.setObjectId(new ObjectId());
        account.setUsername("navras");
        account.setPassword("password");
        account.setEmail("example@abc.com");
        account.setPersonName(personName);
        account.setAddress(address);
        account.setDateOfBirth(date);

        account.setRoles(new HashSet<>(Arrays.asList(AccountRole.ROLE_ADMIN)));

        return account;
    }

    private HttpHeaders getAuthorisedHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        String tokenType = loginResponse.getTokenType().substring(0, 1)
                .toUpperCase() + loginResponse.getTokenType().substring(1);
        httpHeaders.add("Authorization", tokenType + " " + loginResponse.getAccessToken());
        return httpHeaders;
    }

}