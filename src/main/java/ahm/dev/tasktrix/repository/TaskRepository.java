package ahm.dev.tasktrix.repository;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.TaskStatus;
import ahm.dev.tasktrix.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository <Task, Long>{
    Page<Task> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Task> findByUserIdAndStatus(Long userId, TaskStatus status, Pageable pageable);
    Page<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title, Pageable pageable);
    Optional<Task> findByIdAndUserId(Long taskId, Long userId);

    @Query("SELECT tsk FROM Task tsk WHERE tsk.user.id=:userId AND (LOWER(tsk.title) LIKE LOWER(CONCAT('%', :query, '%'))) OR (LOWER(tsk.content) LIKE LOWER(CONCAT('%', :query,'%')))")
    Page<Task> searchTasks(@Param("userId") Long userId, @Param("query") String query, Pageable pageable);

    @Query("SELECT tsk FROM Task tsk WHERE tsk.user.id=:userId AND tsk.dueDate < :now AND tsk.status != 'COMPLETED'")
    List<Task> getOverdueTasks(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT tsk FROM Task tsk WHERE tsk.user.id=:userId AND tsk.status = 'COMPLETED'")
    List<Task> getCompletedTasks(@Param("userId") Long userId);

    @Query("SELECT COUNT(tsk) FROM Task tsk WHERE tsk.user.id=:userId AND tsk.status =:status")
    List<Task> countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") TaskStatus status);

    @Query("SELECT tsk FROM Task tsk WHERE tsk.user.id=:userId AND tsk.dueDate BETWEEN :start AND :end AND tsk.status !='COMPLETED'")
    List<Task> findOverdueTasks(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);



}

