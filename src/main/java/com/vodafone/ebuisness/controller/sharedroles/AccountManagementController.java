package com.vodafone.ebuisness.controller.sharedroles;

import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.service.AuthService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/shared/accounts")
public class AccountManagementController {

    @Autowired
    private AuthService authService;

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK)
    public Account updateAccount(
            @RequestParam("objectId") @NotBlank(message = "Please provide an id") String id
            , @Valid Account account) throws EmailDoesNotExistException {
        account.setObjectId(new ObjectId(id));
        account = authService.update(account);
        return account;

    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteAccount(
            @RequestParam("email") @Email @NotBlank(message = "Please provide an email!") String email)
            throws EmailDoesNotExistException {
        authService.deleteAccount(email);
    }

    @GetMapping("get/id/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Account getAccountById(@PathVariable String id) throws EmailDoesNotExistException {
        return authService.findAccountById(id);
    }

    @GetMapping("get/username/{username}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Account getAccountByUsername(@PathVariable String username) throws EmailDoesNotExistException {
        return authService.findAccountByUsername(username);
    }

    @GetMapping("get/email/{email}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Account getAccountByEmail(@PathVariable String email) throws EmailDoesNotExistException {
        return authService.findAccountByEmail(email);
    }

}
