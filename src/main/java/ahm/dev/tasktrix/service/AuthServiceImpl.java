package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.dto.AuthRequest;
import ahm.dev.tasktrix.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private long jwtexpiration;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
                try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtexpiration)
                    .build();

        } catch (BadCredentialsException e) {
            System.err.println("Authentication failed: Invalid username or password for user: " + authRequest.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public AuthResponse refresh(String token){
        if(jwtService.isRefreshTokenValid(token)){
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            String accessToken = jwtService.generateAccessToken(userDetails);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .expiresIn(jwtexpiration)
                    .build();
        }
        throw new RuntimeException("Invalid token");
    }
}
