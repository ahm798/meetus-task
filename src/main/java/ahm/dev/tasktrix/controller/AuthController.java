package ahm.dev.tasktrix.controller;

import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.AuthRequest;
import ahm.dev.tasktrix.dto.AuthResponse;
import ahm.dev.tasktrix.dto.UserForRegister;
import ahm.dev.tasktrix.service.AuthService;
import ahm.dev.tasktrix.service.JwtServiceImpl;
import ahm.dev.tasktrix.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Register new user", description = "Create a new user account with username, email, and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "text/plain", 
                            examples = @ExampleObject(value = "User registered successfully"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    content = @Content(schema = @Schema(implementation = UserForRegister.class),
                            examples = @ExampleObject(value = "{\"username\":\"john_doe\",\"email\":\"john@example.com\",\"password\":\"password123\"}")))
            @RequestBody UserForRegister userForRegister) {
        userService.createUser(userForRegister);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login credentials",
                    content = @Content(schema = @Schema(implementation = AuthRequest.class),
                            examples = @ExampleObject(value = "{\"username\":\"john_doe\",\"password\":\"password123\"}")))
            @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticate(authRequest);
        return authResponse != null ? ResponseEntity.ok(authResponse) : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Refresh JWT token", description = "Get new access token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "eyJhbGciOiJIUzI1NiJ9...")))
            @RequestBody String refreshRequest) {
        try {
            AuthResponse authResponse = authService.refresh(refreshRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
