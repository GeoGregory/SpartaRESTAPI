package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import com.sparta.api.spartarestapi.entities.CourseEntity;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.exceptions.SpartanNotFoundException;
import com.sparta.api.spartarestapi.repositories.APIKeyRepository;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import com.sparta.api.spartarestapi.factories.SpartansFactory;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
public class SpartanController {

    private final SpartanRepository repository;
    private final CourseRepository courseRepository;
    private final APIKeyRepository apiKeyRepository;

    @Autowired
    public SpartanController(SpartanRepository repository, CourseRepository courseRepository, APIKeyRepository apiKeyRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @GetMapping("/spartans")
    @ResponseBody
    public CollectionModel<EntityModel<SpartanEntity>> getSpartans(@RequestParam Map<String, String> allParams) throws ValidationException {
            SpartansFactory spartanFactory = new SpartansFactory(repository);
            return spartanFactory.getSpartans(allParams);
    }


    @DeleteMapping("/spartans/{id}/{apiKey}")
    public ResponseEntity<?> deleteSpartan(@PathVariable("id") String id, @PathVariable("apiKey") String apiKey) throws ValidationException {
        List<APIKeyEntity> allKeys = apiKeyRepository.findAllByAPIKeyIsNotNull();
        for (APIKeyEntity key: allKeys) {
            if (key.getAPIKey().equals(apiKey)) {
                repository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
        }
        throw new ValidationException("Invalid API Key");
    }

    @GetMapping("spartans/{id}")
    public EntityModel<SpartanEntity> findSpartanById(@PathVariable("id") String id) {
        //add exception
        SpartanEntity spartanEntity = repository.findById(id).orElseThrow(() -> new SpartanNotFoundException(id));
        return EntityModel.of(spartanEntity,
                Link.of("http://localhost:8080/courses/" + spartanEntity.getCourseId()).withRel("course")
        );

    }

    @PostMapping("/spartans/{apiKey}")
    public SpartanEntity addSpartan(@RequestBody SpartanEntity spartan, @PathVariable("apiKey") String apiKey) throws ValidationException {
        List<APIKeyEntity> allKeys = apiKeyRepository.findAllByAPIKeyIsNotNull();
        for (APIKeyEntity key: allKeys) {
            if (key.getAPIKey().equals(apiKey)) {
                if (spartan.getFirstName() != null && spartan.getLastName() != null
                        && spartan.getCourseStartDate() != null && spartan.getCourseId() != null) {
                    if (checkSpartan(spartan)) {
                        CourseEntity course = courseRepository.findByCourseId(spartan.getCourseId()).orElseThrow();
                        if (spartan.getCourseId().equals(course.getCourseId())) {
                            return calculateEndDate(spartan, course.getLength());
                        }
                    }
                    throw new ValidationException("Spartan cannot be created due to invalid details");
                }
            }
        }
        throw new ValidationException("Invalid API Key");
    }

    @PutMapping("/spartans/{apiKey}")
    public ResponseEntity<SpartanEntity> updateSpartan(@RequestBody SpartanEntity updatedSpartan, @PathVariable("apiKey") String apiKey) throws ValidationException {
        List<APIKeyEntity> allKeys = apiKeyRepository.findAllByAPIKeyIsNotNull();
        for (APIKeyEntity key: allKeys) {
            if (key.getAPIKey().equals(apiKey)) {
                if (repository.findById(updatedSpartan.getId()).isPresent()) {
                    SpartanEntity spartan = repository.findById(updatedSpartan.getId()).orElseThrow();
                    if (updatedSpartan.getFirstName() == null) {
                        updatedSpartan.setFirstName(spartan.getFirstName());
                    }
                    if (updatedSpartan.getLastName() == null) {
                        updatedSpartan.setLastName(spartan.getLastName());
                    }
                    if (updatedSpartan.getCourseId() == null) {
                        updatedSpartan.setCourseId(spartan.getCourseId());
                    }
                    if (updatedSpartan.getCourseStartDate() == null) {
                        updatedSpartan.setCourseStartDate(spartan.getCourseStartDate());
                    }
                    if (checkSpartan(updatedSpartan)) {
                        if (updatedSpartan.getCourseEndDate() == null) {
                            CourseEntity course = courseRepository.findByCourseId(updatedSpartan.getCourseId()).orElseThrow();
                            if (updatedSpartan.getCourseId().equals(course.getCourseId())) {
                                return new ResponseEntity<>(calculateEndDate(updatedSpartan, course.getLength()), HttpStatus.OK);
                            }
                        } else {
                            if (LocalDate.parse(updatedSpartan.getCourseEndDate()).isBefore(LocalDate.of(2050, 12, 31))) {
                                return new ResponseEntity<>(repository.save(updatedSpartan), HttpStatus.OK);
                            } else {
                                throw new ValidationException("Spartan cannot be created due to invalid details");
                            }
                        }
                    }
                }
                return new ResponseEntity<>(updatedSpartan, HttpStatus.BAD_REQUEST);
            }
        }
        throw new ValidationException("Invalid API Key");
    }

    private SpartanEntity calculateEndDate(SpartanEntity spartan, int weeksToAdd) throws ValidationException {
        return getSpartanEntity(spartan, weeksToAdd, repository);
    }

    static SpartanEntity getSpartanEntity(SpartanEntity spartan, int weeksToAdd, SpartanRepository spartanRepository) throws ValidationException {
        String endDate = LocalDate.parse(spartan.getCourseStartDate()).plusWeeks(weeksToAdd)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        spartan.setCourseEndDate(String.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        if( LocalDate.parse(spartan.getCourseEndDate()).isBefore(LocalDate.of(2050,12,31))) {
            return spartanRepository.save(spartan);
        }
        else {
            throw new ValidationException("Spartan cannot be created due to invalid details");
        }
    }

    @PostMapping("/spartans/")
    public void spartanWithoutAPIKeyPost() throws ValidationException {
        throw new ValidationException("Need an API Key to perform this action !!!");
    }

    @PutMapping("/spartans/")
    public void spartanWithoutAPIKeyPut() throws ValidationException {
        throw new ValidationException("Need an API Key to perform this action !!!");
    }

    @DeleteMapping("/spartans/{id}")
    public void spartanWithoutAPIKeyPutDelete(@PathVariable("id") String id) throws ValidationException {
        throw new ValidationException("Need an API Key to perform this action !!!");
    }

    private boolean checkSpartan(SpartanEntity spartan) {
        return spartan.getFirstName().length() <= 100 && spartan.getLastName().length() <= 100
                && LocalDate.parse(spartan.getCourseStartDate()).isAfter(LocalDate.of(2022,1,1))
                && spartan.getCourseId() > 0
                && spartan.getCourseId() < courseRepository.findAllByCourseNameIsNotNull().size();
    }

}
