package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpartanRepository extends MongoRepository <SpartanEntity, String> {

    List<SpartanEntity> findAllByFirstnameIsNotNull();
    List<SpartanEntity> findAllByCourseId(Integer courseId);
    List<SpartanEntity> findAllByFirstnameContains(String firstName);
    List<SpartanEntity> findAllByLastNameContains(String lastName);
    List<SpartanEntity> findAllByCourseEndDateIsBefore(LocalDate courseEndDate);
    List<SpartanEntity> findAllByCourseEndDateIsAfter(LocalDate courseEndDate);
    List<SpartanEntity> findAllByCourseStartDateIsBefore(LocalDate courseStartDate);
    List<SpartanEntity> findAllByCourseStartDateIsAfter(LocalDate courseStartDate);
    List<SpartanEntity> findAllByCourseStartDate(LocalDate courseStartDate);
    List<SpartanEntity> findAllByCourseEndDate(LocalDate courseStartDate);
}
