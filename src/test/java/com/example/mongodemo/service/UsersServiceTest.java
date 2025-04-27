package com.example.mongodemo.service;

import com.example.mongodemo.model.Users;
import com.example.mongodemo.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser_Success() {
        Users user = new Users();
        user.setName("Ridzuan");
        user.setEmail("ridzuan@example.com");

        Users savedUser = new Users();
        savedUser.setId("123");
        savedUser.setName("Ridzuan");
        savedUser.setEmail("ridzuan@example.com");

        Mockito.when(usersRepo.save(user)).thenReturn(savedUser);

        Users result = usersService.saveUser(user);

        assertNotNull(result);
        assertEquals("123", result.getId());
        verify(usersRepo, times(1)).save(user);
    }

    @Test
    void findAll() {
    }

    @Test
    public void testDeleteUserById_Success() {
        String userId = "123";

        Mockito.when(usersRepo.existsById(userId)).thenReturn(true);
        Mockito.doNothing().when(usersRepo).deleteById(userId);

        assertDoesNotThrow(() -> usersService.deleteUser(userId));
        verify(usersRepo, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUserById_UserNotFound() {
        String userId = "123";

        Mockito.when(usersRepo.existsById(userId)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> usersService.deleteUser(userId));

        assertTrue(exception.getMessage().contains("Cannot delete. User with id"));
        verify(usersRepo, never()).deleteById(userId);
    }

    @Test
    public void testUpdateUser_Success() {
        String userId = "123";

        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");

        Users updatedUser = new Users();
        updatedUser.setName("New Name");
        updatedUser.setEmail("new@example.com");

        Mockito.when(usersRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(usersRepo.save(any(Users.class))).thenReturn(existingUser);

        Users result = usersService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(usersRepo, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        String userId = "123";
        Users updatedUser = new Users();
        updatedUser.setName("New Name");
        updatedUser.setEmail("new@example.com");

        when(usersRepo.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> usersService.updateUser(userId, updatedUser));

        assertTrue(exception.getMessage().contains("User with id:" + userId + " not found"));
        verify(usersRepo, never()).save(any(Users.class));
    }
}