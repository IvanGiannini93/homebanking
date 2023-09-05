package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    AccountDTO getAccount(Long id);
    List<AccountDTO> getAccount(Authentication authentication);
    ResponseEntity<Object> createAccount(Authentication authentication);
}
