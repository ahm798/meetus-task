package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly =true)
public class UserServiceImpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;


    @Override
    @Cacheable(value = "users", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user by username: {}", username);

        User user = userRepository.findActiveUserByUsername(username)
            .orElseThrow(() -> {
                logger.warn("User not found or inactive: {}", username);
                return new UsernameNotFoundException("User not found with username: " + username);
            });

        logger.debug("User loaded successfully: {}", username);
        return user;
    }

}
