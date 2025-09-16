package ahm.dev.tasktrix.dto;

import ahm.dev.tasktrix.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User registration request")
public class UserForRegister {
    @Schema(description = "Unique username", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "User email address", example = "john@example.com", required = true)
    private String email;
    
    @Schema(description = "User password", example = "password123", required = true)
    private String password;
    
    @Schema(description = "User first name", example = "John")
    private String firstName;
    
    @Schema(description = "User last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "User role", example = "USER")
    private Role role;
}
