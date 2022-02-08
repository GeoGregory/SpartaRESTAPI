package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpartanRepository extends MongoRepository <SpartanEntity, String> {

    List<SpartanEntity> findAllByFirstNameIsNotNull();
    List<SpartanEntity> findAllByCourseId(Integer courseId);
    List<SpartanEntity> findAllByFirstNameContainsIgnoreCase(String firstName);
    List<SpartanEntity> findAllByLastNameContainsIgnoreCase(String lastName);
}
