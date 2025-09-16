package ahm.dev.tasktrix.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
    
    @Value("${jwt.expiration}")
    private long EXPIRATION;
    
    @Value("${jwt.refreshExpiration}")
    private long REFRESH_EXPIRATION;

    private Key getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error creating signing key", e);
            throw new IllegalStateException("Invalid JWT secret key configuration", e);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.warn("Failed to extract claim from token", e);
            throw new IllegalArgumentException("Invalid token format", e);
        }
    }

    private String generateToken(UserDetails userDetails, Map<String, Object> claims, Long expiration) {
        if (userDetails == null || userDetails.getUsername() == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (expiration <= 0) {
            throw new IllegalArgumentException("Expiration must be positive");
        }

        try {
            Instant now = Instant.now();
            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .setClaims(claims)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusMillis(expiration)))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token for user: {}", userDetails.getUsername(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        if (userDetails == null || userDetails.getAuthorities().isEmpty()) {
            throw new IllegalArgumentException("UserDetails must have at least one authority");
        }
        
        Map<String, Object> claims = Map.of(
            "role", userDetails.getAuthorities().iterator().next().getAuthority(),
            "type", "access"
        );

        String token = generateToken(userDetails, claims, EXPIRATION);
        log.debug("Generated access token for user: {}", userDetails.getUsername());
        return token;
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        if (userDetails == null || userDetails.getAuthorities().isEmpty()) {
            throw new IllegalArgumentException("UserDetails must have at least one authority");
        }
        
        Map<String, Object> claims = Map.of(
            "role", userDetails.getAuthorities().iterator().next().getAuthority(),
            "type", "refresh"
        );
        
        String token = generateToken(userDetails, claims, REFRESH_EXPIRATION);
        log.debug("Generated refresh token for user: {}", userDetails.getUsername());
        return token;
    }

    @Override
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.warn("Failed to extract username from token", e);
            return null;
        }
    }

    @Override
    public Claims extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("JWT token has expired", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token", e);
            throw new IllegalArgumentException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token", e);
            throw new IllegalArgumentException("Malformed JWT token", e);
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature", e);
            throw new IllegalArgumentException("Invalid JWT signature", e);
        } catch (Exception e) {
            log.error("Unexpected error parsing JWT token", e);
            throw new RuntimeException("Error parsing JWT token", e);
        }
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (token == null || userDetails == null) {
            return false;
        }
        
        try {
            String tokenUsername = extractUsername(token);
            if (tokenUsername == null || !tokenUsername.equals(userDetails.getUsername())) {
                return false;
            }
            
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            log.debug("Token validation failed for user: {}", 
                userDetails != null ? userDetails.getUsername() : "unknown", e);
            return false;
        }
    }
    
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }
}