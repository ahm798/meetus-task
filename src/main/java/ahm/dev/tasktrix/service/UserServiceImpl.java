package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.UserForRegister;
import ahm.dev.tasktrix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserForRegister userForRegister) {
        if(existsByUsername(userForRegister.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if(existsByEmail(userForRegister.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user =  User.builder()
                .username(userForRegister.getUsername())
                .email(userForRegister.getEmail())
                .password(passwordEncoder.encode(userForRegister.getPassword()))
                .firstName(userForRegister.getFirstName())
                .lastName(userForRegister.getLastName())
                .role(userForRegister.getRole())
                .isActive(true)
                .build();
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + username)
        );
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
