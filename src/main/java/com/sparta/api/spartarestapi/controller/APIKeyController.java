package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.repositories.APIKeyRepository;
import com.sparta.api.spartarestapi.services.GenerateAPIKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIKeyController {

    private final APIKeyRepository apiKeyRepository;

    @Autowired
    public APIKeyController(APIKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @GetMapping("/login")
    public String getAPIKey(@RequestParam String username, @RequestParam String password){
        APIKeyEntity apiKeyEntity = apiKeyRepository.findByUsernameEqualsAndPasswordEquals(username, password);
        apiKeyEntity.setAPIKey(GenerateAPIKeys.generateAPIKey());
        apiKeyRepository.save(apiKeyEntity);
        return "Your API key is: " + apiKeyEntity.getAPIKey();
    }
}
