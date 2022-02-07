package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.entities.TestTest2Entity;
import com.sparta.api.spartarestapi.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestRepository repository;

    @Autowired
    public TestController(TestRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/test")
    public CollectionModel<TestTest2Entity> getTests(){
        System.out.println(repository.findAll().size());
        return CollectionModel.of(repository.findAll());
    }
}
