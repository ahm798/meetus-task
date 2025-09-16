package ahm.dev.tasktrix.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User authentication request")
public class AuthRequest {
    @Schema(description = "Username", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "Password", example = "password123", required = true)
    private String password;
}
