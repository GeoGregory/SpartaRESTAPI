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

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class SpartanController {

    private final SpartanRepository repository;
    private final CourseRepository courseRepository;

    @Autowired
    public SpartanController(SpartanRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/spartans")
    public CollectionModel<SpartanEntity> getSpartans(){
        return CollectionModel.of(repository.findAllByFirstNameIsNotNull());
    }

    @PostMapping("/spartans")
    public SpartanEntity addSpartan(@RequestBody SpartanEntity spartan) throws ValidationException {

        if (spartan.getFirstName() != null && spartan.getLastName() != null
                && spartan.getCourseStartDate() != null && spartan.getCourseId() != null) {
            if(checkSpartan(spartan)){
                CourseEntity course = courseRepository.findByCourseId(spartan.getCourseId()).orElseThrow();
                if( spartan.getCourseId().equals(course.getCourseId())) {
                    return calculateEndDate(spartan, course.getLength());
                }
            }
        }
        throw new ValidationException("Spartan cannot be created due to invalid details");
    }

    @PutMapping("/spartans")
    public ResponseEntity<SpartanEntity> updateSpartan(@RequestBody SpartanEntity updatedSpartan) throws ValidationException {
        if(repository.findById(updatedSpartan.getId()).isPresent()) {
            SpartanEntity spartan = repository.findById(updatedSpartan.getId()).orElseThrow();
            if(updatedSpartan.getFirstName() == null) {
                updatedSpartan.setFirstName(spartan.getFirstName());
            }
            if(updatedSpartan.getLastName() == null) {
                updatedSpartan.setLastName(spartan.getLastName());
            }
            if(updatedSpartan.getCourseId() == null) {
                updatedSpartan.setCourseId(spartan.getCourseId());
            }
            if(updatedSpartan.getCourseStartDate() == null) {
                updatedSpartan.setCourseStartDate(spartan.getCourseStartDate());
            }
            if(checkSpartan(updatedSpartan)) {
                if(updatedSpartan.getCourseEndDate() == null) {
                    CourseEntity course = courseRepository.findByCourseId(updatedSpartan.getCourseId()).orElseThrow();
                    if( updatedSpartan.getCourseId().equals(course.getCourseId())) {
                        return new ResponseEntity<>(calculateEndDate(updatedSpartan, course.getLength()), HttpStatus.OK);
                    }
                } else {
                    if(LocalDate.parse(updatedSpartan.getCourseEndDate()).isBefore(LocalDate.of(2050,12,31))) {
                        return new ResponseEntity<>(repository.save(updatedSpartan), HttpStatus.OK);
                    } else {
                        throw new ValidationException("Spartan cannot be created due to invalid details");
                    }
                }
            }
        }
        return new ResponseEntity<>(updatedSpartan, HttpStatus.BAD_REQUEST);
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

    private boolean checkSpartan(SpartanEntity spartan) {
        return spartan.getFirstName().length() <= 100 && spartan.getLastName().length() <= 100
                && LocalDate.parse(spartan.getCourseStartDate()).isAfter(LocalDate.of(2022,1,1))
                && spartan.getCourseId() > 0
                && spartan.getCourseId() < courseRepository.findAllByCourseNameIsNotNull().size();
    }
}
