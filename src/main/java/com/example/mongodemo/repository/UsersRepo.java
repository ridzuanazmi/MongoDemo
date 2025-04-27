package com.example.mongodemo.repository;

import com.example.mongodemo.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsersRepo extends MongoRepository<Users, String> {

    // Auto-generates: Find users by name
    List<Users> findByName(String name);

    // Find user by email
    Users findByEmail(String email);

    // Find all users whose name contains a keyword (case-sensitive)
    List<Users> findByNameContaining(String keyword);

    // Find all users with email ending with "@gmail.com"
    List<Users> findByEmailEndingWith(String domain);
}
