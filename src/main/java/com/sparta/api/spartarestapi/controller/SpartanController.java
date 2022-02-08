package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpartanController {

    private final SpartanRepository repository;

    @Autowired
    public SpartanController(SpartanRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/spartans")
    public CollectionModel<SpartanEntity> getSpartans(){
        return CollectionModel.of(repository.findAllByFirstnameIsNotNull());
    }

    @DeleteMapping("/spartans/{id}")
    public ResponseEntity<?> deleteSpartan(@PathVariable("id") String id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
