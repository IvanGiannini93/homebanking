package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();

    ClientDTO getClient(Long id);

    void register(String firstName, String lastName, String email, String password);

    ClientDTO current(Authentication authentication);
}
