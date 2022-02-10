package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.exceptions.CourseNotFoundException;
import com.sparta.api.spartarestapi.exceptions.InvalidApiKeyException;
import com.sparta.api.spartarestapi.exceptions.SpartanNotFoundException;
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
            List<CourseEntity> courses = repository.findAllByCourseNameContainsIgnoreCase(courseName);
            if(courses.isEmpty()) {
                throw new CourseNotFoundException("Courses with name : " + courseName + " do not exist");

            } else {
                return CollectionModel.of(courses);
            }
        }
        return CollectionModel.of(repository.findAllByCourseNameIsNotNull());
    }

    @GetMapping("/courses/{courseId}")
    public EntityModel<CourseEntity> findCourseById(@PathVariable("courseId") Integer courseId){

        Link[] links = new Link[spartanRepository.findAllByCourseId(courseId).size()];
        CourseEntity courseEntity = repository.findByCourseId(courseId).orElseThrow(() -> new CourseNotFoundException("Could not find course with id : " + courseId));
        for (int i = 0; i < links.length; i++) {
            SpartanEntity spartan = spartanRepository.findAllByCourseId(courseId).get(i);
            links[i] = linkTo(methodOn(SpartanController.class).findSpartanById(spartan.getId())).withRel("Spartan");
        }
        return EntityModel.of(courseEntity, links);
    }

    @PostMapping("/courses/{apiKey}")
    public CourseEntity addCourse(@RequestBody CourseEntity course, @PathVariable("apiKey") String apiKey) throws ValidationException {
        APIKeyEntity key = apiKeyRepository.findByUsernameEquals("ADMIN");
        if (key.getAPIKey().equals(apiKey)) {
            if (course.getCourseName() != null) {
                if (course.getLength() != null) {
                    if (course.getDescription() != null) {
                        course.setCourseId(repository.findAllByCourseNameIsNotNull().size() + 1);
                        course.setActive(true);
                        return repository.save(course);
                    } throw new ValidationException("Course cannot be created due to description missing.");
                } throw new ValidationException("Course cannot be created due to length missing.");
            } throw new ValidationException("Course cannot be created due to course name missing.");
        } throw new InvalidApiKeyException("A valid ADMIN API key is needed for this feature.");
    }

    @GetMapping("/courses/isActive")
    public CollectionModel<CourseEntity> getActiveCourses(){
        List<CourseEntity> activeCourse = repository.findAllByIsActiveEqualsAndCourseNameIsNotNull(true);
        if(activeCourse.isEmpty()){
            throw new CourseNotFoundException("There are no active courses.");
        }
        return CollectionModel.of(activeCourse);
    }

    @GetMapping("/courses/nonActive")
    public CollectionModel<CourseEntity> getNonActiveCourses(){
        List<CourseEntity> nonActiveCourse = repository.findAllByIsActiveEqualsAndCourseNameIsNotNull(false);
        if (nonActiveCourse.isEmpty()) {
            throw new CourseNotFoundException("No inactive courses available");
        }
        return CollectionModel.of(nonActiveCourse);
    }

    @PutMapping("/courses/{apiKey}")
    public ResponseEntity<CourseEntity> updateCourse(@RequestBody CourseEntity updatedCourse, @PathVariable("apiKey") String apiKey) throws ValidationException {
        APIKeyEntity key = apiKeyRepository.findByUsernameEquals("ADMIN");
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
        throw new InvalidApiKeyException("A valid ADMIN API key is needed for this feature.");
    }

    @DeleteMapping("/courses/{id}/{apiKey}")
    public ResponseEntity<String> deleteCourse(@PathVariable("id") Integer id, @PathVariable("apiKey") String apiKey) throws ValidationException {
        APIKeyEntity key = apiKeyRepository.findByUsernameEquals("ADMIN");
        if (key.getAPIKey().equals(apiKey)) {
            if(repository.findByCourseId(id).isPresent()){
                repository.delete(repository.findByCourseId(id).orElseThrow(() -> new CourseNotFoundException("Course ID not found")));
                return new ResponseEntity<>("Successfully deleted course with id : " + id, HttpStatus.OK);
            } else {
                throw new CourseNotFoundException("Course by id: " + id + " does not exist");
            }
        }
        throw new InvalidApiKeyException("A valid ADMIN API key is needed for this feature.");
    }

    @DeleteMapping("/courses/{id}")
    public void courseWithoutAPIKeyDelete(@PathVariable("id") String id) throws ValidationException {
        throw new InvalidApiKeyException("A valid ADMIN API key is needed for this feature.");
    }

    @PostMapping("/courses/")
    public void courseWithoutAPIKeyPost() throws ValidationException {
        throw new InvalidApiKeyException("A valid ADMIN API key is needed for this feature.");
    }

    @PutMapping("/courses/")
    public void courseWithoutAPIKeyPut() throws ValidationException {
        throw new InvalidApiKeyException("A valid ADMIN API key is needed for this feature.");
    }

    private void updateSpartanEndDate(Integer courseId, Integer length) throws ValidationException {
        List<SpartanEntity> spartans = spartanRepository.findAllByCourseId(courseId);
        for( SpartanEntity spartan : spartans) {
            getSpartanEntity(spartan, length, spartanRepository);
        }
    }

}
