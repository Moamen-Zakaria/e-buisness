package com.vodafone.ebuisness.controller.stateless;

import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.LoginFailException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/auth")
public class AccountsController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MailingService mailingService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Account register(@Valid Account account)
            throws EmailAlreadyExistException {
        authService.register(account);

        new Thread(() -> {
            try {
                mailingService.sendRegistrationEmail(account);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();

        return account;
    }

    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    public Account login(
            @RequestParam("accountId")
            @NotBlank(message = "Invalid account or password") String accountId,
            @NotBlank(message = "Invalid account or password") String password)
            throws LoginFailException {
        var account = authService.login(accountId, password);
        return account;
    }

}
