package com.example.mongodemo.controller;

import com.example.mongodemo.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.mongodemo.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAll() {
        System.out.println("getAll");
        return ResponseEntity.ok(usersService.findAll());
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity<String> createUser(@RequestBody Users user) {
        return ResponseEntity.ok("User " + usersService.saveUser(user) + " created successfully");
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        return usersService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable String id, @RequestBody Users user) {
        return usersService.updateUser(id, user);
    }
}
