package com.vodafone.ebuisness.controller.stateless;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.ebuisness.dto.security.LoginResponse;
import com.vodafone.ebuisness.model.auxiliary.Address;
import com.vodafone.ebuisness.model.auxiliary.Date;
import com.vodafone.ebuisness.model.auxiliary.PersonName;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.repository.AccountRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsControllerTest {

    @MockBean
    private AccountRepository accountRepository;
//
//    @Autowired
//    private AuthService authService;

//    @Autowired
//    private MailingService mailingService;
//
//    @Autowired
//    private JwtTokenProviderImpl jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register() throws Exception {

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        mockMvc.perform(post("/auth/register")
                .param("dateOfBirth.year", "1995")
                .param("dateOfBirth.month", "9")
                .param("dateOfBirth.day", "17")
                .param("email", "example@abc.com")
                .param("username", "navras")
                .param("password", "password")
                .param("personName.firstName", "Moamen")
                .param("personName.middleName", "Mohammed")
                .param("personName.lastName", "Mortada")
                .param("personName.lastName", "Mortada")
                .param("address.province", "Luten")
                .param("address.country", "UK")
                .param("address.street", "sesame street"))
                .andExpect(status().isCreated());

    }

    @Test
    void login() throws Exception {

        when(accountRepository.findByEmailAndPassword("example@abc.com", "password"))
                .thenReturn(getAccount());
        mockMvc.perform(post("/auth/login")
                .param("email", "example@abc.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andExpect(jsonPath("token_type").value("bearer"));

    }

    @Test
    void refresh() throws Exception {

        when(accountRepository.findByEmailAndPassword("example@abc.com", "password"))
                .thenReturn(getAccount());
        var response = mockMvc.perform(post("/auth/login")
                .param("email", "example@abc.com")
                .param("password", "password")).andReturn().getResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginResponse loginResponse
                = objectMapper.readValue(response.getContentAsString(), LoginResponse.class);

        when(accountRepository.findByEmail("example@abc.com")).thenReturn(getAccount());
        mockMvc.perform(post("/auth/refresh/token")
                .param("refresh_token", loginResponse.getRefreshToken())
                .param("email" , "example@abc.com"))
                .andExpect(status().isOk());

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

        return account;
    }

}