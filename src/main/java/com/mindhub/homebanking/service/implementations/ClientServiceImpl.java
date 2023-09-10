package com.mindhub.homebanking.service.implementations;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClient(Long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    public void register(String firstName, String lastName, String email, String password) {
        clientRepository.save(new Client(firstName, lastName, email, password));;
    }

    @Override
    public ClientDTO current(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client != null){
           return new ClientDTO(client);
        }
        return null;
    }
}
