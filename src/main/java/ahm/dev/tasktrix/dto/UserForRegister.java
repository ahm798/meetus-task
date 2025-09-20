package ahm.dev.tasktrix.dto;

import ahm.dev.tasktrix.domain.Role;
import lombok.Data;

@Data
public class UserForRegister {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
}
