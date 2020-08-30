package com.vodafone.ebuisness.controller.sharedroles;

import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.NoAuthenticationFoundException;
import com.vodafone.ebuisness.exception.RefreshTokenNotValidException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.security.util.impl.JwtTokenProviderImpl;
import com.vodafone.ebuisness.service.AuthService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController
@RequestMapping("/shared/accounts")
public class AccountManagementController {

    @Autowired
    private JwtTokenProviderImpl jwtTokenProvider;

    @Autowired
    private AuthService authService;

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK)
    public Account updateAccount(
            Principal principal
            , @Valid Account account) throws EmailDoesNotExistException {
        ObjectId accountId = authService.findAccountByEmail(principal.getName()).getObjectId();
        account.setObjectId(accountId);
        account = authService.update(account);
        return account;

    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteAccount(Principal principal)
            throws EmailDoesNotExistException {
        authService.deleteAccount(principal.getName());
    }

    @PostMapping("/logout")
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(Principal principal)
            throws NoAuthenticationFoundException {
        jwtTokenProvider.logout(principal.getName());
    }

}
