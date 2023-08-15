package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String nameLoan;
    private Double amount;
    private Integer payments;

    public ClientLoanDTO() {
    }
    public ClientLoanDTO(ClientLoan clientLoan) {
        id = clientLoan.getId();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
        loanId = clientLoan.getLoan().getId();
        nameLoan = clientLoan.getLoan().getName();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getNameLoan() {
        return nameLoan;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }
}
