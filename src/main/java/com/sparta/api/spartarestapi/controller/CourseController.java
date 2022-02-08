package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.exceptions.CourseNotFoundException;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
    @ResponseBody
    public CollectionModel<CourseEntity> getCourses(@RequestParam(required = false, value = "name") String courseName){
        if(courseName!=null){
            return CollectionModel.of(
                    repository.findAllByCourseNameContainsIgnoreCase(courseName)
            );
        }
        return CollectionModel.of(repository.findAllByCourseNameIsNotNull());
    }

    @GetMapping("/courses/{courseId}")
    public EntityModel<CourseEntity> findCourseById(@PathVariable("courseId") Integer courseId){
        Link[] links = new Link[spartanRepository.findAllByCourseId(courseId).size()];
        CourseEntity courseEntity = repository.findByCourseId(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
        for (int i = 0; i < links.length; i++) {
            links[i] = linkTo(methodOn(SpartanController.class).findSpartanById(spartanRepository.
                    findAllByCourseId(courseId).get(i).getId())).withRel("Spartan");
        }
        return EntityModel.of(courseEntity, links);
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
