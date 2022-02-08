package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.factories.SpartansFactory;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
public class SpartanController {

    private final SpartanRepository repository;

    @Autowired
    public SpartanController(SpartanRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/spartans")
    @ResponseBody
    public CollectionModel<SpartanEntity> getSpartans(@RequestParam Map<String, String> allParams){
        if (allParams.isEmpty())
            return CollectionModel.of(repository.findAllByFirstNameIsNotNull());
        else{
            SpartansFactory spartanFactory = new SpartansFactory(repository);
            return spartanFactory.getSpartans(allParams);
        }
    }


    @DeleteMapping("/spartans/{id}")
    public ResponseEntity<?> deleteSpartan(@PathVariable("id") String id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/spartans")
    public SpartanEntity addSpartan(@RequestBody SpartanEntity spartan) throws ValidationException {

        if (spartan.getFirstname() != null && spartan.getLastName() != null
                && spartan.getCourseStartDate() != null && spartan.getCourseId() != null) {

            if(checkSpartan(spartan)){


                if( spartan.getCourseId() == 6) {

                    return calculateEndDate(spartan, 5);
                } else {
                    return calculateEndDate(spartan, 8);
                }
            }
        }
        throw new ValidationException("Spartan cannot be created due to invalid details");
    }

    @PutMapping("/spartans")
    public ResponseEntity<SpartanEntity> updateSpartan(@RequestBody SpartanEntity updatedSpartan) throws ValidationException {
        if(repository.findById(updatedSpartan.getId()).isPresent()) {
            SpartanEntity spartan = repository.findById(updatedSpartan.getId()).orElseThrow();
            if(updatedSpartan.getFirstname() == null) {
                updatedSpartan.setFirstname(spartan.getFirstname());
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
                    if( updatedSpartan.getCourseId() == 6) {
                        return new ResponseEntity<>(calculateEndDate(updatedSpartan, 5), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(calculateEndDate(updatedSpartan, 8), HttpStatus.OK);
                    }
                } else {
                    if(LocalDate.parse(spartan.getCourseEndDate()).isBefore(LocalDate.of(2050,12,31))) {
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
        String endDate = LocalDate.parse(spartan.getCourseStartDate()).plusWeeks(weeksToAdd)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        spartan.setCourseEndDate(String.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        if( LocalDate.parse(spartan.getCourseEndDate()).isBefore(LocalDate.of(2050,12,31))) {
            return repository.save(spartan);
        }
        else {
            throw new ValidationException("Spartan cannot be created due to invalid details");
        }
    }

    private boolean checkSpartan(SpartanEntity spartan) {
        return spartan.getFirstname().length() <= 100 && spartan.getLastName().length() <= 100
                && LocalDate.parse(spartan.getCourseStartDate()).isAfter(LocalDate.of(2022,1,1))
                && spartan.getCourseId() > 0
                && spartan.getCourseId() < 7;
    }

}
