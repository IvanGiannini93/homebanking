package com.mindhub.homebanking.service.implementations;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private static final int MAX_ACCOUNTS_PERMITTED = 3;
    private static final double INIT_AMOUNT = 0.0;
    private static final String PREFIX_NUMBER = "VIN-";
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccount(Long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @Override
    public List<AccountDTO> getAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client != null) {
            return client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        int clientAccounts = 0;
        LocalDate createDate = LocalDate.now();
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
