package com.vodafone.ebuisness.controller.admin;

import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
