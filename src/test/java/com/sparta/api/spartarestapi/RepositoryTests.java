package com.sparta.api.spartarestapi;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import com.sparta.api.spartarestapi.repositories.CourseRepository;
import com.sparta.api.spartarestapi.repositories.SpartanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class RepositoryTests {

    @Autowired
    SpartanRepository spartanRepository;
    @Autowired
    CourseRepository courseRepository;

    @Test
    void findAllByFirstNameIsNotNullTest(){
        for (SpartanEntity spartan : spartanRepository.findAllByFirstNameIsNotNull()) {
            Assertions.assertNotNull(spartan.getFirstName());
        }
    }

    @Test
    void findAllByCourseIdTest(){
        for (SpartanEntity spartan : spartanRepository.findAllByCourseId(1)) {
            Assertions.assertEquals(1, spartan.getCourseId());
        }
    }
    @Test
    void findAllByFirstNameContainsIgnoreCaseTest() {
        for (SpartanEntity spartan : spartanRepository.findAllByFirstNameContainsIgnoreCase("h")) {
            Assertions.assertTrue(spartan.getFirstName().toLowerCase().contains("h"));
        }
    }
    @Test
    void findAllByLastNameContainsIgnoreCaseTest(){
        for (SpartanEntity spartan : spartanRepository.findAllByLastNameContainsIgnoreCase("h")) {
            Assertions.assertTrue(spartan.getLastName().toLowerCase().contains("h"));
        }
    }
    @Test
    void findAllByCourseNameIsNotNullTest(){
        Assertions.assertEquals(14, courseRepository.findAllByCourseNameIsNotNull().size());
    }
    @Test
    void findByCourseIdTest(){
        for (int i = 0; i < courseRepository.findAllByCourseNameContainsIgnoreCase("Java").size(); i++) {
            Assertions.assertEquals("Java", courseRepository.findAllByCourseNameContainsIgnoreCase("Java").get(i).getCourseName());
        }
    }

}
