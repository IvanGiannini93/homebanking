package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> transaction(@RequestParam Double ammount, @RequestParam String description,
                                              @RequestParam String fromNumber, @RequestParam String toNumber,
                                              Authentication authentication){
        LocalDateTime dateTime = LocalDateTime.now();
        if(authentication == null) {
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
        if(ammount == null || description.isEmpty() || fromNumber.isEmpty() || toNumber.isEmpty() ||
                fromNumber.equals(toNumber)) {
            return new ResponseEntity<>("Invalid request.", HttpStatus.BAD_REQUEST);
        }
        Account fromAccount = accountRepository.findByNumber(fromNumber);
        if(fromAccount == null) {
            return new ResponseEntity<>("Non-existent origin account", HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client == null) {
            return new ResponseEntity<>("Invalid client", HttpStatus.UNAUTHORIZED);
        }
        if(!client.getAccounts().contains(fromAccount)) {
            return new ResponseEntity<>("Invalid account", HttpStatus.FORBIDDEN);
        }
        Account toAccount = accountRepository.findByNumber(toNumber);
        if(toAccount == null) {
            return new ResponseEntity<>("Non-existent destination account", HttpStatus.FORBIDDEN);
        }
        if(fromAccount.getBalance() < ammount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, "desc", ammount * -1, dateTime, fromAccount);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, "desc", ammount, dateTime, toAccount);
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        fromAccount.setBalance(debitTransaction.getAmount());
        toAccount.setBalance(creditTransaction.getAmount());
        fromAccount.addTransactions(debitTransaction);
        toAccount.addTransactions(creditTransaction);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return new ResponseEntity<>("Successful operation", HttpStatus.CREATED);
    }
}
