package ahm.dev.tasktrix;

import ahm.dev.tasktrix.controller.AuthController;
import ahm.dev.tasktrix.dto.UserForRegister;
import ahm.dev.tasktrix.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private UserForRegister userForRegister;

    @BeforeEach
    void setUp() {
        userForRegister = new UserForRegister();
        userForRegister.setUsername("testuser");
        userForRegister.setEmail("test@example.com");
        userForRegister.setPassword("password123");
        userForRegister.setFirstName("John");
        userForRegister.setLastName("Doe");
    }

    @Test
    @DisplayName("register should return success message and call createUser when valid user provided")
    void register_WithValidUser_ShouldReturnSuccessMessage() {
        // When
        ResponseEntity<String> response = authController.register(userForRegister);

        // Then
        assertEquals("User registered successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).createUser(userForRegister);
    }

    @Test
    @DisplayName("register should still call userService even with minimal data")
    void register_WithMinimalData_ShouldCallUserService() {
        // Given
        UserForRegister minimalUser = new UserForRegister();
        minimalUser.setUsername("user");
        minimalUser.setEmail("user@test.com");
        minimalUser.setPassword("pass");

        // When
        ResponseEntity<String> response = authController.register(minimalUser);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userService, times(1)).createUser(minimalUser);
    }

    @Test
    @DisplayName("register should handle service exceptions gracefully")
    void register_WhenServiceThrowsException_ShouldPropagateException() {
        // Given
        doThrow(new RuntimeException("User already exists")).when(userService).createUser(userForRegister);

        // When & Then
        assertThrows(RuntimeException.class, () -> authController.register(userForRegister));
        verify(userService, times(1)).createUser(userForRegister);
    }

    @Test
    @DisplayName("register should handle null user input")
    void register_WithNullUser_ShouldCallServiceWithNull() {
        // When
        ResponseEntity<String> response = authController.register(null);

        // Then
        assertEquals("User registered successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).createUser(null);
    }
}