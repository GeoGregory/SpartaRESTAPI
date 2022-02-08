package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("courses/{id}")
    public EntityModel<CourseEntity> findCourseById(@PathVariable("id") Integer id) {
        //add exception
        CourseEntity courseEntity = repository.findById(String.valueOf(id)).orElseThrow();
        return EntityModel.of(courseEntity

        );

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
