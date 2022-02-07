package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.TestTest2Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends MongoRepository <TestTest2Entity, Integer> {
}
