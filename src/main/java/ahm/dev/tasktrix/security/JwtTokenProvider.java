package ahm.dev.tasktrix.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.jsonwebtoken.*;
import ahm.dev.tasktrix.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }

    private String generateToken(Authentication authentication, Long expiration){
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        try {
            return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
        } catch (Exception e) {
            logger.error("Failed to generate JWT token for user: {}", userPrincipal.getUsername(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    private Claims getALlClaimsFromToken(String token){
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getALlClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(Authentication authentication){
        return generateToken(authentication, jwtProperties.expiration());
    }

    public String generateRefreshToken(Authentication authentication){
        return generateToken(authentication, jwtProperties.refreshExpiration());
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getExpiredDateFromToken(String token){
        return getClaimFromToken(token, claims -> claims.getExpiration().toString());
    }

    public boolean isTokenExpired(String token){
        return getClaimFromToken(token, Claims::getExpiration).before(Date.from(Instant.now()));
    }

    public boolean validateToken(String authToken){
        try{
            Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
