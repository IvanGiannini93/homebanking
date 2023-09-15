package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }
    @GetMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> getAccount(Authentication authentication){
        if(authentication == null){
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
        List<AccountDTO> accounts = accountService.getAccount(authentication);
        if(accounts != null){
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid user", HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        if(authentication != null){
            return accountService.createAccount(authentication);
        }
        else {
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
    }

}
