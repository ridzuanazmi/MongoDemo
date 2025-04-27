package com.example.mongodemo.service;

import com.example.mongodemo.model.Users;
import org.springframework.stereotype.Service;
import com.example.mongodemo.repository.UsersRepo;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepo usersRepo;

    public UsersService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Users saveUser(Users user) {
        Users savedUser = usersRepo.save(user);
        if (savedUser.getId() == null) {
            throw new RuntimeException("Failed to save user.");
        }
        return savedUser;
    }

    public List<Users> findAll() {
        return usersRepo.findAll();
    }

    public String deleteUser(String id) {
        if (!usersRepo.existsById(id)) {
            throw new RuntimeException("Cannot delete. User with id " + id + " not found.");
        }
        usersRepo.deleteById(id);
        return "User with id:" + id + " deleted successfully";
    }

    public Users updateUser(String id, Users userToUpdate) {
        Users existingUser = usersRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id:" + id + " not found"));

        existingUser.setName(userToUpdate.getName());
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setAge(userToUpdate.getAge());

        Users savedUser = usersRepo.save(existingUser);
        if (savedUser.getId() == null) {
            throw new RuntimeException("Failed to update user.");
        }
        return savedUser;
    }
}
