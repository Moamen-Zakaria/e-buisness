package com.vodafone.ebuisness.controller.stateless;

import com.vodafone.ebuisness.configuration.AccountRole;
import com.vodafone.ebuisness.dto.security.LoginResponse;
import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.LoginFailException;
import com.vodafone.ebuisness.exception.NoAuthenticationFoundException;
import com.vodafone.ebuisness.exception.RefreshTokenNotValidException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.security.util.impl.JwtTokenProviderImpl;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AccountsController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private JwtTokenProviderImpl jwtTokenProvider;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Account register(@Valid Account account)
            throws EmailAlreadyExistException {
        Set<String> roles = new HashSet<>();
        roles.add(AccountRole.ROLE_USER);
        account.setRoles(roles);
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
    public LoginResponse login(
            @RequestParam @NotBlank(message = "Invalid account or password") String email,
            @RequestParam @NotBlank(message = "Invalid account or password") String password)
            throws LoginFailException {
        var account = authService.login(email, password);
        var roles = account.getRoles() == null ? new ArrayList<String>() : account.getRoles();
        var rolesList = roles.stream().collect(Collectors.toList());
        var accessToken = jwtTokenProvider.createAccessToken(account.getEmail(), rolesList);
        var refreshToken = jwtTokenProvider.createNewRefreshToken(account.getEmail());
        return new LoginResponse(accessToken, refreshToken, "bearer");
    }

    @PostMapping("/refresh/token")
    @ResponseStatus(value = HttpStatus.OK)
    public String refresh(
            @RequestParam @NotNull @NotBlank(message = "No valid email provided") String email,
            @RequestParam("refresh_token") @NotNull @NotBlank(message = "No valid token provided") String refreshToken)
            throws RefreshTokenNotValidException {
        return jwtTokenProvider.refreshToken(email, refreshToken);
    }

}
