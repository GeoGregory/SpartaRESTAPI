package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.SpartanEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpartanRepository extends MongoRepository <SpartanEntity, String> {

    List<SpartanEntity> findAllByFirstnameIsNotNull();
}
