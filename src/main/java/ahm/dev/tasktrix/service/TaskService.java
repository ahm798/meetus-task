package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.TaskStatus;
import ahm.dev.tasktrix.dto.TaskCreateRequest;
import ahm.dev.tasktrix.dto.TaskResponse;
import ahm.dev.tasktrix.dto.TaskUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskResponse createTask(TaskCreateRequest request, Long userId);
    Page<TaskResponse> getUsertasks(Long userId, Pageable pageable);
    TaskResponse updateTask(Long taskId, TaskUpdateRequest request, Long userId);
    void deleteTask(Long taskId, Long userId);
    Page<TaskResponse> getTasksByStatus(Long userId, TaskStatus status, Pageable pageable);
    Page<TaskResponse> searchTasks(Long userId, String query, Pageable pageable);
    TaskResponse getTaskById(Long taskId, Long userId);
}
