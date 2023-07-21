package com.projects.cannon.VideoShare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.projects.cannon.VideoShare.model.User;

public interface UserRepository extends MongoRepository<User,String> {
}
