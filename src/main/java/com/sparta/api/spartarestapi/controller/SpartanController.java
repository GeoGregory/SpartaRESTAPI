package com.sparta.api.spartarestapi.controller;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public CollectionModel<SpartanEntity> getSpartans(){
        return CollectionModel.of(repository.findAllByFirstnameIsNotNull());
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
