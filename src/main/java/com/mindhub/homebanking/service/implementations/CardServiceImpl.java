package com.mindhub.homebanking.service.implementations;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private static final int MAX_CARDS_PERMITTED = 3;
    private static final int YEAR_TO_EXPIRED = 5;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<CardDTO> getCards(Client client) {
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @Override
    public void createCard(CardType cardType, CardColor cardColor, Client client) {
        String cardHolder = "";
        String number = "";
        int cvv;
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(YEAR_TO_EXPIRED);
        cardHolder = client.getFirstName().toUpperCase() + " " + client.getLastName().toUpperCase();
        number = CardUtils.getCardNumber();
        while(cardRepository.existsByNumber(number)){
            number = CardUtils.getCardNumber();
        }
        cvv = CardUtils.getCvv();
        Card newCard = new Card(cardHolder, cardType, cardColor, number, cvv, thruDate, fromDate, client);
        cardRepository.save(newCard);
        client.addCard(newCard);
        clientRepository.save(client);
    }

}
