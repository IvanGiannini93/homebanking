package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    private static final int MAX_ACCOUNTS_PERMITTED = 3;
    private static final double INIT_AMOUNT = 0.0;
    private static final String PREFIX_NUMBER = "VIN-";
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }
    @RequestMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> getAccount(Authentication authentication){
        if(authentication == null){
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client != null){
            List<AccountDTO> accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid user", HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        int clientAccounts = 0;
        LocalDate createDate = LocalDate.now();
        if(authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            if(client != null){
                clientAccounts = client.getAccounts().size();
                if(clientAccounts < MAX_ACCOUNTS_PERMITTED){
                    String number = generateRandomNumber();
                    Account newAccount = new Account(number, createDate, INIT_AMOUNT, client);
                    accountRepository.save(newAccount);
                    System.out.println(newAccount.getId());
                    client.addAccount(newAccount);
                    clientRepository.save(client);
                    return new ResponseEntity<>("Create account success", HttpStatus.CREATED);
                }
                else {
                    return new ResponseEntity<>("Exceeds max number account permitted", HttpStatus.FORBIDDEN);
                }
            }
            else {
                return new ResponseEntity<>("User not registered", HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
    }

    private String generateRandomNumber(){
        String numberAccount = "";
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        int randomNumber = random.nextInt(max - min + 1) + min;
        numberAccount = PREFIX_NUMBER + randomNumber;
        if(accountRepository.existsByNumber(numberAccount)){
            generateRandomNumber();
        }
        return numberAccount;
    }
}
