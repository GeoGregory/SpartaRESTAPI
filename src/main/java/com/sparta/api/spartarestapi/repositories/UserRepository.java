package com.sparta.api.spartarestapi.repositories;

import com.sparta.api.spartarestapi.entities.UsersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UsersEntity, Integer> {
}
