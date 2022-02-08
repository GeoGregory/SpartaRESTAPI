package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository <CourseEntity, String> {

    List<CourseEntity> findAllByCourseNameIsNotNull();
    Optional<CourseEntity> findByCourseId(Integer courseId);
}
