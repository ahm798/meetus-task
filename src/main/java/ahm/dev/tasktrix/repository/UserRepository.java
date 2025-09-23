package ahm.dev.tasktrix.repository;

import ahm.dev.tasktrix.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.enabled = true")
    Optional<User> findActiveUserByUsername(@Param("username") String username);

    @Modifying
    @Query("UPDATE User u SET u.credentialsNonExpired = false WHERE u.updatedAt < :cutoffDate")
    int markExpiredCredentials(@Param("cutoffDate") LocalDateTime cutoffDate);
}