package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import net.bytebuddy.asm.Advice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository,
								  TransactionRepository transactionRepository, LoanRepository loanRepository,
								  ClientLoanRepository clientLoanRepository){
		return args -> {
			Client client = new Client();
			client.setFirstName("Melba");
			client.setLastName("Morel");
			client.setEmail("melba.morel@gmail.com");
			clientRepository.save(client);

			Client client2 = new Client();
			client2.setFirstName("Homer");
			client2.setLastName("Simpson");
			client2.setEmail("homer.simpson@gmail.com");
			clientRepository.save(client2);

			LocalDate date1 = LocalDate.now();
			Account account1 = new Account("VIN001", date1, 5000.0, client);
			LocalDate date2 = date1.plusDays(1);
			Account account2 = new Account("VIN002", date2, 7500.0, client);
			Account account3 = new Account("VIN003", date1, 12000.0, client2);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			TransactionType debit = TransactionType.DEBIT;
			TransactionType credit = TransactionType.CREDIT;
			LocalDateTime dateTime = LocalDateTime.now();
			Transaction transaction1 = new Transaction(debit, "transferencia", 2000.0, dateTime, account1);
			Transaction transaction2 = new Transaction(credit, "transferencia", 3000.0, dateTime, account1);
			Transaction transaction3 = new Transaction(credit, "transferencia", 1000.0, dateTime, account2);
			Transaction transaction4 = new Transaction(credit, "transferencia", 6000.0, dateTime, account3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			List<Integer> installments1 = List.of(12,24,36,48,60);
			List<Integer> installments2 = List.of(6,12,24);
			List<Integer> installments3 = List.of(6,12,24,36);
			Loan loan1 = new Loan("Hipotecario", 500000.0, installments1);
			Loan loan2 = new Loan("Personal", 100000.0, installments2);
			Loan loan3 = new Loan("Automotriz", 300000.0, installments3);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client, loan2);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, client2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, client2, loan3);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

		};
	}
}
