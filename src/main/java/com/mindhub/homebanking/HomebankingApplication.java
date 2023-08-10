package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository,
								  TransactionRepository transactionRepository){
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
		};
	}
}
