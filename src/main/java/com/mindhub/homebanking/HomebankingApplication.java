package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository){
		return args -> {
			Client client = new Client();
			client.setFirstName("Melba");
			client.setLastName("Morel");
			client.setEmail("melba.morel@gmail.com");
			clientRepository.save(client);

			LocalDate date1 = LocalDate.now();
			Account account1 = new Account("VIN001", date1, 5000.0, client);
			LocalDate date2 = date1.plusDays(1);
			Account account2 = new Account("VIN002", date2, 7500.0, client);
			accountRepository.save(account1);
			accountRepository.save(account2);
		};
	}
}
