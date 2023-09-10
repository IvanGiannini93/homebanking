package com.mindhub.homebanking.service.implementations;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    private static final double PERCENT_INTEREST = 1.2;
    private static final String LOAN_APPROVED = "Loan Approved";
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public void createLoan(Client client, Loan loan, LoanApplicationDTO loanApplicationDTO) {
        ClientLoan clientLoan = new ClientLoan();
        clientLoan.setLoan(loan);
        clientLoan.setClient(client);
        clientLoan.setAmount(loanApplicationDTO.getAmount() * PERCENT_INTEREST);
        clientLoan.setPayments(loanApplicationDTO.getPayments());
        clientLoanRepository.save(clientLoan);

        Account account = client.getAccounts().stream().filter(clientAccount ->
                        clientAccount.getNumber().equals(loanApplicationDTO.getToAccountNumber()))
                        .collect(Collectors.toList()).get(0);

        Transaction transaction = new Transaction();
        transaction.setAmount(clientLoan.getAmount());
        transaction.setAccount(account);
        transaction.setDate(LocalDateTime.now());
        transaction.setType(TransactionType.CREDIT);
        transaction.setDescription(LOAN_APPROVED);
        transactionRepository.save(transaction);

        account.setBalance(clientLoan.getAmount());
        accountRepository.save(account);
    }
}
