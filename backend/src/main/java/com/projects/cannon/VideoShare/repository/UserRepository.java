package com.projects.cannon.VideoShare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.projects.cannon.VideoShare.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findBySub(String sub);
}
