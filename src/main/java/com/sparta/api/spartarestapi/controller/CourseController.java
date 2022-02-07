package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    private final CourseRepository repository;

    @Autowired
    public CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/course")
    public CollectionModel<CourseEntity> getSpartans(){
        System.out.println(repository.findAll().size());
        return CollectionModel.of(repository.findAllByCourseNameIsNotNull());
    }
}
