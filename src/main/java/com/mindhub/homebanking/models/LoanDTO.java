package com.mindhub.homebanking.models;

import java.util.List;

public class LoanDTO {
    private String name;
    private Double maxAmount;
    private List<Integer> payments;

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

}
