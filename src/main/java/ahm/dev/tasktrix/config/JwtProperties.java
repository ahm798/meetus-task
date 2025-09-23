package ahm.dev.tasktrix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, Long expiration, Long refreshExpiration) {
    @ConstructorBinding
    public JwtProperties{
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret cannot be null");
        }
        if (expiration <= 0) {
            throw new IllegalArgumentException("Expiration must be positive");
        }
        if (refreshExpiration <= 0) {
            throw new IllegalArgumentException("Refresh expiration must be positive");
        }
    }
}
