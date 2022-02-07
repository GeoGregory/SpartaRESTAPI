package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository <CourseEntity, String> {
}
