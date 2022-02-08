package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

import static com.sparta.api.spartarestapi.controller.SpartanController.getSpartanEntity;

@RestController
public class CourseController {

    private final CourseRepository repository;
    private final SpartanRepository spartanRepository;

    @Autowired
    public CourseController(CourseRepository repository, SpartanRepository spartanRepository) {
        this.repository = repository;
        this.spartanRepository = spartanRepository;
    }

    @GetMapping("/courses")
    public CollectionModel<CourseEntity> getCourses(){
        return CollectionModel.of(repository.findAllByCourseNameIsNotNull());
    }


    @GetMapping("courses/isActive")
    public CollectionModel<CourseEntity> getActiveCourses(){
        List<CourseEntity> activeCourses = new ArrayList<>();
        for(CourseEntity course : repository.findAllByCourseNameIsNotNull()){
            if(course.getActive()){
                activeCourses.add(course);
            }
        }
        return CollectionModel.of(activeCourses);
    }
    @GetMapping("courses/nonActive")
    public CollectionModel<CourseEntity> getNonActiveCourses(){
        List<CourseEntity> nonActiveCourses = new ArrayList<>();
        for(CourseEntity course : repository.findAllByCourseNameIsNotNull()){
            if(!course.getActive()){
                nonActiveCourses.add(course);
            }
        }
        return CollectionModel.of(nonActiveCourses);
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

    @PutMapping("/courses")
    public ResponseEntity<CourseEntity> updateCourse(@RequestBody CourseEntity updatedCourse) throws ValidationException {
        if(repository.findById(updatedCourse.getId()).isPresent()) {
            CourseEntity course = repository.findById(updatedCourse.getId()).orElseThrow();
            if(updatedCourse.getCourseName() == null) {
                updatedCourse.setCourseName(course.getCourseName());
            }
            if(updatedCourse.getDescription() == null) {
                updatedCourse.setDescription(course.getDescription());
            }
            if(updatedCourse.getLength() == null) {
                updatedCourse.setLength(course.getLength());
            } else {
                updateSpartanEndDate(updatedCourse.getCourseId(), updatedCourse.getLength());
            }
            if(updatedCourse.getActive() == null) {
                updatedCourse.setActive(course.getActive());
            }
            return new ResponseEntity<>(repository.save(updatedCourse), HttpStatus.OK);
        }
        return new ResponseEntity<>(updatedCourse, HttpStatus.BAD_REQUEST);
    }

    private void updateSpartanEndDate(Integer courseId, Integer length) throws ValidationException {
        List<SpartanEntity> spartans = spartanRepository.findAllByCourseId(courseId);
        for( SpartanEntity spartan : spartans) {
            getSpartanEntity(spartan, length, spartanRepository);
        }
    }



}
