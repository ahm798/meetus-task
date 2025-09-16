package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.UserForRegister;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(UserForRegister userForRegister);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User getCurrentUser();
}
