package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.LoanDTO;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoans();
    void createLoan(Client client, Loan loan, LoanApplicationDTO loanApplicationDTO);
}
