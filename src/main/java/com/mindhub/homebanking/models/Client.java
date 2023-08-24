package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client(){}

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Client(String firstName, String lastName, String email, Set<Account> accounts,
                  Set<ClientLoan> clientLoans, Set<Card> cards) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = accounts;
        this.clientLoans = clientLoans;
        this.cards = cards;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccount(Account account) {
        this.accounts.add(account);
    }

    public List<ClientLoan> getLoans() {
        return clientLoans.stream().collect(Collectors.toList());
    }

    public void addClientLoans(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        card.setClient(this);
        this.cards.add(card);
    }

}
