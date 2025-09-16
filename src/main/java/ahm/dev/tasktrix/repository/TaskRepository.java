package ahm.dev.tasktrix.repository;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository <Task, Long>{
    List<Task> findByUserId(Long userId);
    Optional<Task> findByIdAndUser(Long id, User user);
}
