package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.UsersEntity;
import com.sparta.api.spartarestapi.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    @PostMapping("/addUser")
    public String saveUser(@RequestBody UsersEntity user){
        repository.save(user);
        return "Added user with id : " + user.getId();
    }


}
