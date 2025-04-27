package com.example.mongodemo.service;

import com.example.mongodemo.exception.UserAlreadyExistsException;
import com.example.mongodemo.exception.UserCouldNotBeSavedException;
import com.example.mongodemo.exception.UserNotFoundException;
import com.example.mongodemo.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.mongodemo.repository.UsersRepo;

import java.util.List;

@Service
@Slf4j
public class UsersService {

    private final UsersRepo usersRepo;

    public UsersService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Users saveUser(Users user) {
        // Check if email already exists
        log.info("Saving user {}", user);
        Users existingUser = usersRepo.findByEmail(user.getEmail());
        if (existingUser != null) {
            log.warn("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        Users savedUser = usersRepo.save(user);
        if (savedUser.getId() == null) {
            log.error("User with email {} was not saved", user.getEmail());
            throw new UserCouldNotBeSavedException("Failed to save user.");
        }
        log.info("User with email {} saved", user.getEmail());
        return savedUser;
    }

    public List<Users> findAll() {
        log.info("Finding all users");
        return usersRepo.findAll();
    }

    public List<Users> searchByName(String name) {
        List<Users> users = usersRepo.findByName(name);
        if (users == null || users.isEmpty()) {
            log.error("User with name {} was not found", name);
            throw new UserNotFoundException("No users found with name: " + name);
        }
        log.info("User(s) found with name {}", name);
        return users;
    }

    public Users getUserByEmail(String email) {
        Users user = usersRepo.findByEmail(email);
        if (user == null) {
            log.error("User with email {} was not found", email);
            throw new UserNotFoundException("User not found with email: " + email);
        }
        log.info("User with email {} found", email);
        return user;
    }

    public String deleteUser(String id) {
        if (!usersRepo.existsById(id)) {
            log.error("User with id {} was not found", id);
            throw new UserNotFoundException("Cannot delete. User with id " + id + " not found.");
        }
        usersRepo.deleteById(id);
        log.info("User with id {} deleted", id);
        return "User with id:" + id + " deleted successfully";
    }

    public Users updateUser(String id, Users userToUpdate) {
        Users existingUser = usersRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id {} was not found", id);
                    return new UserNotFoundException("User with id:" + id + " not found");
                });

        existingUser.setName(userToUpdate.getName());
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setAge(userToUpdate.getAge());

        Users savedUser = usersRepo.save(existingUser);
        if (savedUser.getId() == null) {
            log.error("User with id {} could not be saved", id);
            throw new UserCouldNotBeSavedException("Failed to update user.");
        }
        log.info("User with id {} updated", id);
        return savedUser;
    }
}
