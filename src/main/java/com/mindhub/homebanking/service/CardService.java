package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CardService {
    List<CardDTO> getCards(Client client);
    void createCard(CardType cardType, CardColor cardColor, Client client);

}
