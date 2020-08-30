package com.vodafone.ebuisness.controller.admin;

import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.service.AuthService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/admin/auth")
public class AuthorisationManagementController {

    @Autowired
    AuthService authService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/grant")
    public void grantRole(@RequestParam @NotBlank String email,
                          @RequestParam @NotBlank String role)
            throws EmailDoesNotExistException {
        authService.grantRole(email, role);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/revoke")
    public void revokeRole(@RequestParam @NotBlank String email,
                           @RequestParam @NotBlank String role)
            throws EmailDoesNotExistException {
        authService.revokeRole(email, role);
    }

    @GetMapping("/hasRole/{email}/{role}")
    public Boolean hasRole(@PathVariable @NotBlank String email
            , @PathVariable @NotBlank String role) throws EmailDoesNotExistException {

        return authService.hasRole(email, role);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts(){
        return authService.getAllAccounts();
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

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK)
    public Account updateAccount(
            @RequestParam("account_id") @NotBlank(message = "Please provide an id") String accountId
            , @Valid Account account) throws EmailDoesNotExistException {
        account.setObjectId(new ObjectId(accountId));
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

}
