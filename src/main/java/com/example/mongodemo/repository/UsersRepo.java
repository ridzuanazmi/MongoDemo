package com.example.mongodemo.repository;

import com.example.mongodemo.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepo extends MongoRepository<Users, String> {
}
