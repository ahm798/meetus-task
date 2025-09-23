package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.TaskStatus;
import ahm.dev.tasktrix.dto.TaskCreateRequest;
import ahm.dev.tasktrix.dto.TaskResponse;
import ahm.dev.tasktrix.dto.TaskUpdateRequest;
import ahm.dev.tasktrix.exception.ResourceNotFoundException;
import ahm.dev.tasktrix.exception.UnauthorizedAccessException;
import ahm.dev.tasktrix.repository.TaskRepository;
import ahm.dev.tasktrix.mapper.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    @CacheEvict(value = "user_tasks", key = "#userId")
    public TaskResponse createTask(TaskCreateRequest request, Long userId) {
        logger.info("Creating task for user {}", userId);

        Task task = new Task();
        task.getUser().setId(userId);
        Task savedTask = taskRepository.save(task);
        logger.info("Task created with ID {}", savedTask.getId());
        return taskMapper.toResponse(savedTask);
    }

    @Cacheable(value = "user_tasks", key = "#userId +'_'+ #pageable.pageNumber")
    public Page<TaskResponse> getUsertasks(Long userId, Pageable pageable){
        logger.info("Getting tasks for user {}", userId);
        Page<Task> tasks = taskRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "user-tasks", key = "#userId")
    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request, Long userId) {
        logger.info("Updating task {} for user {}", taskId, userId);

        Task existingTask = findTaskByIdAndUserId(taskId, userId);
        taskMapper.updateEntityFromRequest(request, existingTask);

        Task updatedTask = taskRepository.save(existingTask);
        logger.info("Task {} updated successfully", taskId);

        return taskMapper.toResponse(updatedTask);
    }

    @Transactional
    @CacheEvict(value = "user-tasks", key = "#userId")
    public void deleteTask(Long taskId, Long userId) {
        logger.info("Deleting task {} for user {}", taskId, userId);

        Task task = findTaskByIdAndUserId(taskId, userId);
        taskRepository.delete(task);

        logger.info("Task {} deleted successfully", taskId);
    }

    public Page<TaskResponse> getTasksByStatus(Long userId, TaskStatus status, Pageable pageable) {
        logger.debug("Fetching tasks with status {} for user {}", status, userId);

        Page<Task> tasks = taskRepository.findByUserIdAndStatus(userId, status, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    public Page<TaskResponse> searchTasks(Long userId, String query, Pageable pageable) {
        logger.debug("Searching tasks for user {} with query: {}", userId, query);

        Page<Task> tasks = taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, query, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    public TaskResponse getTaskById(Long taskId, Long userId) {
        logger.debug("Fetching task {} for user {}", taskId, userId);

        Task task = findTaskByIdAndUserId(taskId, userId);
        return taskMapper.toResponse(task);
    }


    public List<TaskResponse> getOverdueTasks(Long userId) {
        logger.debug("Fetching overdue tasks for user: {}", userId);

        List<Task> tasks = taskRepository.findOverdueTasks(userId, LocalDateTime.now(), LocalDateTime.now());
        return tasks.stream().map(taskMapper::toResponse).collect(java.util.stream.Collectors.toList());
    }

    public Page<TaskResponse> getCompletedTasks(Long userId, Pageable pageable) {
        logger.debug("Fetching completed tasks for user: {}", userId);
        Page<Task> tasks = taskRepository.findByUserIdAndStatus(userId, TaskStatus.COMPLETED, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    private Task findTaskByIdAndUserId(Long taskId, Long userId) {
        Optional<Task> taskOpt = taskRepository.findByIdAndUserId(taskId, userId);

        if (taskOpt.isEmpty()) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }

        Task task = taskOpt.get();
        if (!task.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Access denied to task: " + taskId);
        }
        return task;
    }
}
