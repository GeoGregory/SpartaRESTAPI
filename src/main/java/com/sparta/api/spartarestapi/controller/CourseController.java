package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.ValidationException;

@RestController
public class CourseController {

    private final CourseRepository repository;

    @Autowired
    public CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/courses")
    public CollectionModel<CourseEntity> getSpartans(){
        return CollectionModel.of(repository.findAllByCourseNameIsNotNull());
    }

    @PostMapping("/courses")
    public CourseEntity addCourse(@RequestBody CourseEntity course) throws ValidationException {

        if (course.getCourseName() != null && course.getLength() != null
                && course.getDescription() != null) {
            course.setCourseId(repository.findAllByCourseNameIsNotNull().size() + 1);
            course.setActive(true);
            return repository.save(course);
        }

        throw new ValidationException("Course cannot be created due to invalid input");
    }
}
