package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public ResponseEntity<Object> getLoans(){
        return new ResponseEntity<>(loanRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping(path = "/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        if(authentication == null){
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO == null || loanApplicationDTO.getToAccountNumber() == null || loanApplicationDTO.getToAccountNumber().isEmpty() ||
        loanApplicationDTO.getAmount() == null || loanApplicationDTO.getAmount() < 0 || loanApplicationDTO.getLoanId() == null ||
        loanApplicationDTO.getPayments() == null || loanApplicationDTO.getPayments() < 0){
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        if(!loanRepository.existsById(loanApplicationDTO.getLoanId())){
            return new ResponseEntity<>("Non-existent loan", HttpStatus.BAD_REQUEST);
        }
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).get();
        if(loan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("The requested amount exceeded",HttpStatus.BAD_REQUEST);
        }
        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Number of installments not available",HttpStatus.BAD_REQUEST);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client == null){
            return new ResponseEntity<>("User not registered", HttpStatus.UNAUTHORIZED);
        }
        List<String> numberAccounts = client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList());
        if(!numberAccounts.contains(loanApplicationDTO.getToAccountNumber())){
            return new ResponseEntity<>("The account number does not belong to the client", HttpStatus.FORBIDDEN);
        }
        loanService.createLoan(client, loan, loanApplicationDTO);
        return new ResponseEntity<>("Loan approved.", HttpStatus.CREATED);
    }
}
