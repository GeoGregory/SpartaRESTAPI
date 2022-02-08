package com.sparta.api.spartarestapi.factories;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.hateoas.CollectionModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class SpartansFactory {
    private final SpartanRepository spartanRepository;

    public SpartansFactory(SpartanRepository spartanRepository) {
        this.spartanRepository = spartanRepository;
    }

    public CollectionModel<SpartanEntity> getSpartans(Map<String, String> spartansParameters){
        // below if statement may be redundant
        if(spartansParameters.isEmpty()){
            return CollectionModel.of(spartanRepository.findAll());
        }
        List<SpartanEntity> allSpartans = spartanRepository.findAll();


        if(spartansParameters.get("firstName")!=null){
            List<SpartanEntity> spartansByFirstName = spartanRepository.findAllByFirstnameContains(spartansParameters.get("firstName"));
            allSpartans.retainAll(spartansByFirstName);
        }
        if (spartansParameters.get("lastName") != null){
            List<SpartanEntity> spartansByLastName = spartanRepository.findAllByLastNameContains(spartansParameters.get("lastName"));
            allSpartans.retainAll(spartansByLastName);
        }
        if (spartansParameters.get("date") !=null && spartansParameters.get("beforeAfter") != null && spartansParameters.get("startEnd") != null){
            if (spartansParameters.get("beforeAfter").equals("before")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    List<SpartanEntity> spartansBeforeStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isBefore(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeStart);
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    List<SpartanEntity> spartansBeforeStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isBefore(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeStart);
                }
            } else if (spartansParameters.get("beforeAfter").equals("after")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    List<SpartanEntity> spartansBeforeStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isAfter(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeStart);
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    List<SpartanEntity> spartansBeforeStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isAfter(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeStart);
                }
            } else if (spartansParameters.get("beforeAfter").equals("now")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    List<SpartanEntity> spartansBeforeStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isEqual(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeStart);
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    List<SpartanEntity> spartansBeforeStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isEqual(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeStart);
                }
            }
        }
        
        return CollectionModel.of(allSpartans);
        
    }
}

