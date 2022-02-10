package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.exceptions.CourseNotFoundException;
import com.sparta.api.spartarestapi.repositories.APIKeyRepository;
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
import java.util.ArrayList;
import java.util.List;

import static com.sparta.api.spartarestapi.controller.SpartanController.getSpartanEntity;

@RestController
public class CourseController {

    private final CourseRepository repository;
    private final SpartanRepository spartanRepository;
    private final APIKeyRepository apiKeyRepository;

    @Autowired
    public CourseController(CourseRepository repository, SpartanRepository spartanRepository, APIKeyRepository apiKeyRepository) {
        this.repository = repository;
        this.spartanRepository = spartanRepository;
        this.apiKeyRepository = apiKeyRepository;
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
            SpartanEntity spartan = spartanRepository.findAllByCourseId(courseId).get(i);
            links[i] = linkTo(methodOn(SpartanController.class).findSpartanById(spartan.getId())).withRel("Spartan");
        }
        return EntityModel.of(courseEntity, links);
    }

    @PostMapping("/courses/{apiKey}")
    public CourseEntity addCourse(@RequestBody CourseEntity course, @PathVariable("apiKey") String apiKey) throws ValidationException {
        List<APIKeyEntity> allKeys = apiKeyRepository.findAllByAPIKeyIsNotNull();
        for (APIKeyEntity key: allKeys) {
            if (key.getAPIKey().equals(apiKey)) {
                if (course.getCourseName() != null && course.getLength() != null
                        && course.getDescription() != null) {
                    course.setCourseId(repository.findAllByCourseNameIsNotNull().size() + 1);
                    course.setActive(true);
                    return repository.save(course);
                }
                throw new ValidationException("Course cannot be created due to invalid input");
            }
        }
        throw new ValidationException("Invalid API Key");
    }

    @GetMapping("/courses/isActive")
    public CollectionModel<CourseEntity> getActiveCourses(){
        List<CourseEntity> activeCourse = new ArrayList<>();
        for (CourseEntity course : repository.findAllByCourseNameIsNotNull()){
            if(course.getActive()){
                activeCourse.add(course);
            }
        }
        return CollectionModel.of(activeCourse);
    }

    @GetMapping("/courses/nonActive")
    public CollectionModel<CourseEntity> getNonActiveCourses() throws ValidationException{
        List<CourseEntity> nonActiveCourse = new ArrayList<>();
        for (CourseEntity course : repository.findAllByCourseNameIsNotNull()){
            if(!course.getActive()){
                nonActiveCourse.add(course);
            }
        }
        if (nonActiveCourse.isEmpty()) {
            throw new ValidationException("No non-active courses available");
        }
        return CollectionModel.of(nonActiveCourse);
    }

    @PutMapping("/courses/{apiKey}")
    public ResponseEntity<CourseEntity> updateCourse(@RequestBody CourseEntity updatedCourse, @PathVariable("apiKey") String apiKey) throws ValidationException {
        List<APIKeyEntity> allKeys = apiKeyRepository.findAllByAPIKeyIsNotNull();
        for (APIKeyEntity key: allKeys) {
            if (key.getAPIKey().equals(apiKey)) {
                if (repository.findById(updatedCourse.getId()).isPresent()) {
                    CourseEntity course = repository.findById(updatedCourse.getId()).orElseThrow();
                    if (updatedCourse.getCourseName() == null) {
                        updatedCourse.setCourseName(course.getCourseName());
                    }
                    if (updatedCourse.getDescription() == null) {
                        updatedCourse.setDescription(course.getDescription());
                    }
                    if (updatedCourse.getActive() == null) {
                        updatedCourse.setActive(course.getActive());
                    }
                    if (updatedCourse.getCourseId() == null) {
                        updatedCourse.setCourseId(course.getCourseId());
                    }
                    if (updatedCourse.getLength() == null) {
                        updatedCourse.setLength(course.getLength());
                    } else {
                        updateSpartanEndDate(updatedCourse.getCourseId(), updatedCourse.getLength());
                    }
                    return new ResponseEntity<>(repository.save(updatedCourse), HttpStatus.OK);
                }
                return new ResponseEntity<>(updatedCourse, HttpStatus.BAD_REQUEST);
            }
        }
        throw new ValidationException("Invalid API Key");
    }

    @PostMapping("/courses/")
    public void courseWithoutAPIKeyPost() throws ValidationException {
        throw new ValidationException("Need an API Key to perform this action !!!");
    }

    @PutMapping("/courses/")
    public void courseWithoutAPIKeyPut() throws ValidationException {
        throw new ValidationException("Need an API Key to perform this action !!!");
    }

    private void updateSpartanEndDate(Integer courseId, Integer length) throws ValidationException {
        List<SpartanEntity> spartans = spartanRepository.findAllByCourseId(courseId);
        for( SpartanEntity spartan : spartans) {
            getSpartanEntity(spartan, length, spartanRepository);
        }
    }

}
