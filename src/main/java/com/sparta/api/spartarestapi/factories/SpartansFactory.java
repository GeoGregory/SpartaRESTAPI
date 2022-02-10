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
        List<SpartanEntity> correctSpartans = new ArrayList<>();

        if(spartansParameters.get("firstName")!=null){
            List<SpartanEntity> spartansByFirstName = spartanRepository.findAllByFirstNameContainsIgnoreCase(spartansParameters.get("firstName"));
            if(spartansByFirstName.size() == 0){
                throw new ValidationException("Spartan with first name " + spartansParameters.get("firstName") + " does not exist in the database.");
            }
            allSpartans.retainAll(spartansByFirstName);
        }
        if (spartansParameters.get("lastName") != null){
            List<SpartanEntity> spartansByLastName = spartanRepository.findAllByLastNameContainsIgnoreCase(spartansParameters.get("lastName"));
            if(spartansByLastName.size() == 0){
                throw new ValidationException("Spartan with last name " +spartansParameters.get("lastName") + " does not exist in the database.");
            }
            allSpartans.retainAll(spartansByLastName);
        }
        if (spartansParameters.get("date") !=null && spartansParameters.get("beforeAfter") != null && spartansParameters.get("startEnd") != null){
            if (spartansParameters.get("beforeAfter").equals("before")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isBefore(LocalDate.parse(spartansParameters.get("date").trim())))
                                correctSpartans.add(spartan);
                        }
                    }
                    if(correctSpartans.isEmpty()){
                        throw new ValidationException("Spartans starting before " +spartansParameters.get("date") + " do not exist in the database.");
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isBefore(LocalDate.parse(spartansParameters.get("date").trim())))
                                correctSpartans.add(spartan);
                        }
                    }
                    if(correctSpartans.isEmpty()){
                        throw new ValidationException("Spartans finishing before " +spartansParameters.get("date") + " do not exist in the database.");
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                }
            } else if (spartansParameters.get("beforeAfter").equals("after")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isAfter(LocalDate.parse(spartansParameters.get("date").trim())))
                                correctSpartans.add(spartan);
                        }
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                    if(correctSpartans.isEmpty()){
                        throw new ValidationException("Spartans starting after " +spartansParameters.get("date") + " do not exist in the database.");
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isAfter(LocalDate.parse(spartansParameters.get("date").trim())))
                                correctSpartans.add(spartan);
                        }
                    }
                    if(correctSpartans.isEmpty()){
                        throw new ValidationException("Spartans finishing after " +spartansParameters.get("date") + " do not exist in the database.");
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                }
            } else if (spartansParameters.get("beforeAfter").equals("now")){
                if (spartansParameters.get("startEnd").equals("start")) {
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseStartDate().trim()).isEqual(LocalDate.parse(spartansParameters.get("date").trim())))
                                correctSpartans.add(spartan);
                        }
                    }
                    if(correctSpartans.isEmpty()){
                        throw new ValidationException("Spartans starting on " +spartansParameters.get("date") + " do not exist in the database.");
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                }
                else if (spartansParameters.get("startEnd").equals("end")) {
                    for (SpartanEntity spartan: allSpartans) {
                        if (spartan.getCourseStartDate() != null && spartan.getCourseEndDate() != null) {
                            if (LocalDate.parse(spartan.getCourseEndDate().trim()).isEqual(LocalDate.parse(spartansParameters.get("date").trim())))
                                correctSpartans.add(spartan);
                        }
                    }
                    if(correctSpartans.isEmpty()){
                        throw new ValidationException("Spartans finishing on " +spartansParameters.get("date") + " do not exist in the database.");
                    }
                    allSpartans.retainAll(correctSpartans);
                    correctSpartans.clear();
                }
            }
        }

        if (spartansParameters.get("active") != null) {
            if (spartansParameters.get("active").equals("true")) {
                for (SpartanEntity spartan : allSpartans) {
                    if (LocalDate.parse(spartan.getCourseStartDate()).isBefore(LocalDate.now()) &&
                            LocalDate.parse(spartan.getCourseEndDate()).isAfter(LocalDate.now())) {
                        correctSpartans.add(spartan);
                    }
                }
                if(correctSpartans.isEmpty()){
                    throw new ValidationException("There are no active Spartans in the database.");
                }
                allSpartans.retainAll(correctSpartans);
                correctSpartans.clear();
            } else if (spartansParameters.get("active").equals("false")) {
                for (SpartanEntity spartan : allSpartans) {
                    if (LocalDate.parse(spartan.getCourseStartDate()).isAfter(LocalDate.now()) ||
                            LocalDate.parse(spartan.getCourseEndDate()).isBefore(LocalDate.now())) {
                        correctSpartans.add(spartan);
                    }
                }
                if(correctSpartans.isEmpty()){
                    throw new ValidationException("There are no inactive Spartans in the database.");
                }
                allSpartans.retainAll(correctSpartans);
                correctSpartans.clear();

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

