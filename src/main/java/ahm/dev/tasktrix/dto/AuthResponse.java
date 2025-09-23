package ahm.dev.tasktrix.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    String accessToken,

    String refreshToken,

    String tokenType,

    UserInfo user,

    LocalDateTime expiresAt
) {
    public record UserInfo(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName
    ) {}
}