package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    private static final int MAX_CARDS_PERMITTED = 3;
    private static final int YEAR_TO_EXPIRED = 5;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                             Authentication authentication){
        String cardHolder = "";
        String number = "";
        int cvv;
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(YEAR_TO_EXPIRED);
        if(authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            if(client != null){
                Set<Card> cards = client.getCards();
                List<Card> cardsByType = cards.stream().filter(card ->
                        card.getCardType().name().equals(type.name())).collect(Collectors.toList());
                if(cardsByType.size() < MAX_CARDS_PERMITTED){
                    if(type != null && !type.name().isEmpty() && color != null && !color.name().isEmpty()){
                        cardHolder = client.getFirstName().toUpperCase() + " " + client.getLastName().toUpperCase();
                        number = generateNumber();
                        cvv = generateCvv();
                        Card newCard = new Card(cardHolder, type, color, number, cvv, thruDate, fromDate, client);
                        cardRepository.save(newCard);
                        client.addCard(newCard);
                        clientRepository.save(client);
                        return new ResponseEntity<>("Card create success", HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>("Type or color invalid", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>("Exceeds max number card permitted", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("User not registered", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
    }


    private String generateNumber() {
        String number = "";
        int min = 1000;
        int max = 9999;
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            number += random.nextInt(max - min + 1) + min + "-";
        }
        number = number.substring(0, number.length() - 2);
        return number;
    }
    private int generateCvv() {
        int min = 100;
        int max = 999;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
