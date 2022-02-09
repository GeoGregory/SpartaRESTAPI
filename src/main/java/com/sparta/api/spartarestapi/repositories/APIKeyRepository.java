package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.APIKeyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface APIKeyRepository extends MongoRepository<APIKeyEntity, String> {
    List<APIKeyEntity> findAllByAPIKeyIsNotNull();
}
