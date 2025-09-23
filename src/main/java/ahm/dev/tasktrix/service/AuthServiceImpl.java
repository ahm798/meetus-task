package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.AuthResponse;
import ahm.dev.tasktrix.dto.LoginRequest;
import ahm.dev.tasktrix.dto.SignupRequest;
import ahm.dev.tasktrix.exception.BusinessLogicException;
import ahm.dev.tasktrix.mapper.UserMapper;
import ahm.dev.tasktrix.repository.UserRepository;
import ahm.dev.tasktrix.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl{
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    public void registerUser(SignupRequest signupRequest) {
        logger.info("Attempting to register user: {}", signupRequest.username());

        if (userRepository.existsByUsername(signupRequest.username())) {
            throw new BusinessLogicException("USER_EXISTS", "Username is already taken");
        }

        if (userRepository.existsByEmail(signupRequest.email())) {
            throw new BusinessLogicException("EMAIL_EXISTS", "Email is already registered");
        }

        User user = userMapper.toEntity(signupRequest);
        user.setPassword(passwordEncoder.encode(signupRequest.password()));

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
    }

    @Transactional(readOnly = true)
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.username());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(), loginRequest.password()
                )
            );
        } catch (AuthenticationException ex) {
            logger.warn("Authentication failed for user: {}", loginRequest.username());
            throw new BusinessLogicException("AUTH_FAILED", "Invalid username or password");
        }

        User user = (User) authentication.getPrincipal();
        logger.info("User authenticated successfully with ID: {}", user.getId());

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        LocalDateTime expiresAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(System.currentTimeMillis() + 86400000), // 24 hours
            ZoneId.systemDefault()
        );

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
        return new AuthResponse(accessToken, refreshToken, "Bearer", userInfo, expiresAt);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken)) {
            throw new BusinessLogicException("INVALID_TOKEN", "Invalid refresh token");
        }

        if (tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
            throw new BusinessLogicException("TOKEN_BLACKLISTED", "Token has been blacklisted");
        }

        User user = userRepository.findActiveUserByUsername(tokenProvider.getUsernameFromToken(refreshToken))
            .orElseThrow(() -> new BusinessLogicException("USER_NOT_FOUND", "User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String newAccessToken = tokenProvider.generateAccessToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

        tokenBlacklistService.blacklistToken(refreshToken);

        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis() + 86400000),
                ZoneId.systemDefault()
        );

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer", userInfo, expiresAt);
    }

    public void logout(String token) {
        tokenBlacklistService.blacklistToken(token);
    }
}

