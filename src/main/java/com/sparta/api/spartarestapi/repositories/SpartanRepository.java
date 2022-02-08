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
    List<SpartanEntity> findAllByFirstNameContains(String firstname);
    List<SpartanEntity> findAllByLastNameContains(String lastName);
    List<SpartanEntity> findAllByFirstnameContainsAndLastNameContains(String firstname, String lastName);
    List<SpartanEntity> findAllByCourseEndDateIsBefore(LocalDate courseEndDate);
    List<SpartanEntity> findAllByCourseEndDateIsAfter(LocalDate courseEndDate);
    List<SpartanEntity> findAllByCourseStartDateIsBefore(LocalDate courseStartDate);
    List<SpartanEntity> findAllByCourseStartDateIsAfter(LocalDate courseStartDate);
    List<SpartanEntity> findAllByFirstnameContainsAndCourseEndDateIsBefore(String firstname,LocalDate courseEndDate);
    List<SpartanEntity> findAllByFirstNameContainsAAndCourseEndDateIsAfter(String firstname, LocalDate courseEndDate);
    List<SpartanEntity> findAllByLastNameContainsAAndCourseEndDateIsBefore(String lastName, LocalDate courseEndDate);
    List<SpartanEntity> findAllByLastNameContainsAAndCourseEndDateIsAfter(String lastName, LocalDate courseEndDate);
    List<SpartanEntity> findAllByFirstnameContainsAndCourseStartDateIsBefore(String firstname,LocalDate courseStartDate);
    List<SpartanEntity> findAllByFirstNameContainsAAndCourseStartDateIsAfter(String firstname, LocalDate courseStartDate);
    List<SpartanEntity> findAllByLastNameContainsAAndCourseStartDateIsBefore(String lastName, LocalDate courseStartDate);
    List<SpartanEntity> findAllByLastNameContainsAAndCourseStartDateIsAfter(String lastName, LocalDate courseStartDate);
    List<SpartanEntity> findAllByCourseStartDateIsBeforeAndAndCourseEndDateIsAfter(LocalDate courseStartDate, LocalDate courseEndDate);
    List<SpartanEntity> findAllByFirstnameContainsAndLastNameContainsAndCourseEndDateIsBefore(String firstname, String lastName, LocalDate courseEndDate);
    List<SpartanEntity> findAllByFirstnameContainsAndLastNameContainsAndCourseEndDateIsAfter(String firstname, String lastName,LocalDate courseEndDate);
    List<SpartanEntity> findAllByFirstnameContainsAndLastNameContainsAndCourseStartDateIsBefore(String firstname, String lastName, LocalDate courseStartDate);
    List<SpartanEntity> findAllByFirstnameContainsAndLastNameContainsAndCourseStartDateIsAfter(String firstname, String lastName, LocalDate courseStartDate);
}
