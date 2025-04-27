package com.example.mongodemo.service;

import com.example.mongodemo.model.Users;
import com.example.mongodemo.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
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
    public void testSaveUser_Failure_NullId() {
        // Arrange
        Users userToSave = new Users();
        userToSave.setName("Ridzuan");
        userToSave.setEmail("ridzuan@example.com");

        Users savedUser = new Users(); // Simulate MongoDB save returns a user without ID
        savedUser.setName("Ridzuan");
        savedUser.setEmail("ridzuan@example.com");

        when(usersRepo.save(userToSave)).thenReturn(savedUser);

        // Act + Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usersService.saveUser(userToSave);
        });

        assertTrue(exception.getMessage().contains("Failed to save user."));
        verify(usersRepo, times(1)).save(userToSave);
    }

    @Test
    public void testFindAllUsers_Success() {
        // Arrange: Create sample data
        Users user1 = new Users();
        user1.setId("1");
        user1.setName("Ridzuan");
        user1.setEmail("ridzuan@example.com");

        Users user2 = new Users();
        user2.setId("2");
        user2.setName("Azmi");
        user2.setEmail("azmi@example.com");

        List<Users> usersList = List.of(user1, user2);

        when(usersRepo.findAll()).thenReturn(usersList);

        // Act: Call the service method
        List<Users> result = usersService.findAll();

        // Assert: Check result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ridzuan", result.get(0).getName());
        assertEquals("Azmi", result.get(1).getName());

        // Verify interaction
        verify(usersRepo, times(1)).findAll();
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