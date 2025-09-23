package ahm.dev.tasktrix.controller;

import ahm.dev.tasktrix.dto.*;
import ahm.dev.tasktrix.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private final AuthServiceImpl authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid SignupRequest signupRequest) {
             authService.registerUser(signupRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.success("User registered successfully", null)
            );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response=  authService.authenticateUser(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Authentication successful", response)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = extractTokenFromHeader(authHeader);
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader) {

        String token = extractTokenFromHeader(authHeader);
        authService.logout(token);

        logger.info("User logged out successfully");

        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }


    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header format");
        }
        return authHeader.substring(7);
    }

}
