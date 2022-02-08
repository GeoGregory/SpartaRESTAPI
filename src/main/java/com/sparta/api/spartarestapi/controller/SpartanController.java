package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.factories.SpartansFactory;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
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

    /*@GetMapping("/spartans")
    public CollectionModel<SpartanEntity> getSpartans(){
        SpartansFactory spartansFactory = new SpartansFactory(repository);
        return spartansFactory.getSpartans()
    }*/

    @GetMapping("/spartans")
    @ResponseBody
    public CollectionModel<SpartanEntity> getSpartans(@RequestParam Map<String, String> allParams){
        if (allParams.isEmpty())
            return CollectionModel.of(repository.findAllByFirstnameIsNotNull());
        else{
            SpartansFactory spartanFactory = new SpartansFactory(repository);
            return spartanFactory.getSpartans(allParams);
        }
    }


    @DeleteMapping("/spartans/{id}")
    public ResponseEntity<?> deleteSpartan(@PathVariable("id") String id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/spartans/{id}")
    public ResponseEntity<?> deleteSpartan(@PathVariable("id") String id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
