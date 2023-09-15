package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    private CardService cardService;


    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCards(Authentication authentication){
        if(authentication == null){
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
        Client client= clientRepository.findByEmail(authentication.getName());
        if(client != null){
            List<CardDTO> cards = cardService.getCards(client);
            return new ResponseEntity<>(cards, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid user", HttpStatus.FORBIDDEN);
    }
    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor,
                                             Authentication authentication){

        if(authentication != null){
            if (cardType == null || cardType.name().isEmpty() || cardColor == null || cardColor.name().isEmpty()) {
                return new ResponseEntity<>("Type or color invalid", HttpStatus.BAD_REQUEST);
            }
            Client client = clientRepository.findByEmail(authentication.getName());
            if(client == null) {
                return new ResponseEntity<>("User not registered", HttpStatus.UNAUTHORIZED);
            }
            Set<Card> cards = client.getCards();
            List<Card> cardsByType = cards.stream().filter(card ->
                    card.getCardType().name().equals(cardType.name())).collect(Collectors.toList());
            if (cardsByType.size() == MAX_CARDS_PERMITTED){
                return new ResponseEntity<>("Exceeds max number card permitted", HttpStatus.FORBIDDEN);
            }
            cardService.createCard(cardType,cardColor,client);
            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Not connection", HttpStatus.FORBIDDEN);
        }
    }

}
