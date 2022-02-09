package com.sparta.api.spartarestapi.factories;

import com.sparta.api.spartarestapi.controller.SpartanController;
import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class SpartansFactory {
    private final SpartanRepository spartanRepository;

    public SpartansFactory(SpartanRepository spartanRepository) {
        this.spartanRepository = spartanRepository;
    }

    public CollectionModel<EntityModel<SpartanEntity>> getSpartans(Map<String, String> spartansParameters) throws ValidationException {
        // below if statement may be redundant
        if(spartansParameters.isEmpty()){
            return CollectionModel.of(getEntityModelList(spartanRepository.findAllByFirstNameIsNotNull()));
        }
        List<SpartanEntity> allSpartans = spartanRepository.findAllByFirstNameIsNotNull();


        if(spartansParameters.get("firstName")!=null){
            List<SpartanEntity> spartansByFirstName = spartanRepository.findAllByFirstNameContainsIgnoreCase(spartansParameters.get("firstName"));
            allSpartans.retainAll(spartansByFirstName);
        }
        if (spartansParameters.get("lastName") != null){
            List<SpartanEntity> spartansByLastName = spartanRepository.findAllByLastNameContainsIgnoreCase(spartansParameters.get("lastName"));
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
                    List<SpartanEntity> spartansBeforeEnd = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isBefore(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansBeforeEnd.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansBeforeEnd);
                }
            } else if (spartansParameters.get("beforeAfter").equals("after")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    List<SpartanEntity> spartansAfterStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isAfter(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansAfterStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansAfterStart);
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    List<SpartanEntity> spartansAfterEnd = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isAfter(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansAfterEnd.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansAfterEnd);
                }
            } else if (spartansParameters.get("beforeAfter").equals("now")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    List<SpartanEntity> spartansNowStart = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isEqual(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansNowStart.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansNowStart);
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    List<SpartanEntity> spartansNowEnd = new ArrayList<>();
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isEqual(LocalDate.parse(spartansParameters.get("date").trim())))
                                spartansNowEnd.add(spartan);
                        }
                    }
                    allSpartans.retainAll(spartansNowEnd);
                }
            }
        }
        if(spartansParameters.get("active") !=null) {
            if (spartansParameters.get("active").equals("true")) {
                List<SpartanEntity> activeSpartans = new ArrayList<>();
                for (SpartanEntity spartan : allSpartans) {
                    if (LocalDate.parse(spartan.getCourseStartDate()).isBefore(LocalDate.now()) &&
                            LocalDate.parse(spartan.getCourseEndDate()).isAfter(LocalDate.now())) {
                        activeSpartans.add(spartan);
                    }
                }
                allSpartans.retainAll(activeSpartans);
            } else if (spartansParameters.get("active").equals("false")) {
                List<SpartanEntity> inactiveSpartans = new ArrayList<>();
                for (SpartanEntity spartan : allSpartans) {
                    if (LocalDate.parse(spartan.getCourseStartDate()).isAfter(LocalDate.now()) ||
                            LocalDate.parse(spartan.getCourseEndDate()).isBefore(LocalDate.now())) {
                        inactiveSpartans.add(spartan);
                    }
                }
                allSpartans.retainAll(inactiveSpartans);
            }
        }
        return CollectionModel.of(getEntityModelList(allSpartans));
    }

    private List<EntityModel<SpartanEntity>> getEntityModelList(List<SpartanEntity> spartanEntities) throws ValidationException {
        EntityModel<SpartanEntity>[] entityModels = new EntityModel[spartanEntities.size()];
        for (int i = 0; i < spartanEntities.size(); i++) {
            Link[] links = new Link[2];
            links[0] = Link.of("http://localhost:8080/courses/" + spartanEntities.get(i).getCourseId()).withRel("course");
            links[1] = linkTo(methodOn(SpartanController.class).findSpartanById(spartanEntities.get(i).getId())).withSelfRel();
           entityModels[i] = EntityModel.of(spartanEntities.get(i), links);
        }
        return Arrays.stream(entityModels).toList();
    }
}

