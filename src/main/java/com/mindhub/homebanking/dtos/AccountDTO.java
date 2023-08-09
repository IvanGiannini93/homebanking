package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {

    private Long id;
    private String number;
    private LocalDate date;
    private Double balance;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        id = account.getId();
        number = account.getNumber();
        date = account.getCreationDate();
        balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }
    public Double getBalance() {
        return balance;
    }


}
