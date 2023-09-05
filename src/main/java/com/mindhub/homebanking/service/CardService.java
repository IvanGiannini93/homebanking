package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    List<CardDTO> getCards(Authentication authentication);
}
