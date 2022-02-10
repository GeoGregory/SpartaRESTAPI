package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.exceptions.SpartanNotFoundException;
import com.sparta.api.spartarestapi.repositories.APIKeyRepository;
import com.sparta.api.spartarestapi.services.GenerateAPIKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

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

    @DeleteMapping("/apiKeys/{apiKey}/{adminApiKey}")
    public String deleteApiKey(@PathVariable("apiKey") String apiKey, @PathVariable("adminApiKey") String adminApiKey){
        APIKeyEntity key = apiKeyRepository.findByUsernameEquals("ADMIN");
        if (key.getAPIKey().equals(adminApiKey)) {
            if(apiKeyRepository.findByAPIKey(apiKey).getAPIKey().equals(apiKey)){
                apiKeyRepository.deleteByAPIKey(apiKey);
                return "Successfully deleted API Key with apiKey : " + apiKey;
            } else {
                return "Could Not Find API with apiKey: " + apiKey;
            }
        }
        else
            return "Require a Admin API key to perform this action";
    }
    @GetMapping("/apiKeys/{adminApiKey}")
    public List<APIKeyEntity> getApiKeys(@PathVariable("adminApiKey") String apiKey) throws ValidationException{
        APIKeyEntity key = apiKeyRepository.findByUsernameEquals("ADMIN");
        if (key.getAPIKey().equals(apiKey)) {
            return apiKeyRepository.findAllByAPIKeyIsNotNull();
        }
        throw new ValidationException("A valid API key is needed for this feature");
    }

}
