package com.example.mongodemo.controller;

import com.example.mongodemo.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.mongodemo.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAll() {
        log.info("Getting all users");
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping("/search")
    public List<Users> searchUserByName(@RequestParam String name) {
        log.info("searching user by name {}", name);
        return usersService.searchByName(name);
    }

    @GetMapping("/email")
    public Users getUserByEmail(@RequestParam String email) {
        log.info("searching user by email {}", email);
        return usersService.getUserByEmail(email);
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity<String> createUser(@RequestBody Users user) {
        log.info("creating user {}", user);
        return ResponseEntity.ok("User " + usersService.saveUser(user) + " created successfully");
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        log.info("deleting user {}", id);
        return usersService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable String id, @RequestBody Users user) {
        log.info("updating user {}", id);
        return usersService.updateUser(id, user);
    }
}
