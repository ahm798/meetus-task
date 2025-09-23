package ahm.dev.tasktrix.controller;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.*;
import ahm.dev.tasktrix.exception.BusinessLogicException;
import ahm.dev.tasktrix.service.TaskService;
import ahm.dev.tasktrix.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskCreateRequest request,
            Authentication authentication
    ){
        Long userId = ((User)authentication.getPrincipal()).getId();
        TaskResponse response = taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Task created successfully", response)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTasks(
            @PageableDefault(size = 10, sort = "createdAt", direction= Sort.Direction.DESC) Pageable pageable
            ,Authentication authentication
    ){

        Long userId = ((User)authentication.getPrincipal()).getId();

        Page<TaskResponse> tasks = taskService.getUsertasks(userId, pageable);
        PageResponse<TaskResponse> responses = PageResponse.of(tasks);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("Tasks retrieved successfully", responses)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuthentication(authentication);
        logger.debug("Fetching task {} for user: {}", id, userId);

        TaskResponse taskResponse = taskService.getTaskById(id, userId);

        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", taskResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request,
            Authentication authentication) {

        Long userId = extractUserIdFromAuthentication(authentication);
        logger.info("Updating task {} for user: {}", id, userId);

        TaskResponse updatedTask = taskService.updateTask(id, request, userId);

        logger.info("Task {} updated successfully", id);

        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", updatedTask));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuthentication(authentication);
        logger.info("Deleting task {} for user: {}", id, userId);

        taskService.deleteTask(id, userId);

        logger.info("Task {} deleted successfully", id);

        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully", null));
    }

        @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> searchTasks(
            @RequestParam String q,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            Authentication authentication) {

        Long userId = extractUserIdFromAuthentication(authentication);
        logger.debug("Searching tasks for user {} with query: {}", userId, q);

        Page<TaskResponse> tasks = taskService.searchTasks(userId, q, pageable);
        PageResponse<TaskResponse> pageResponse = PageResponse.of(tasks);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", pageResponse));
    }

//    @GetMapping("/overdue")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getOverdueTasks(
//            @PageableDefault(size = 20, sort = "dueDate", direction = Sort.Direction.ASC)
//            Pageable pageable,
//            Authentication authentication) {
//
//        Long userId = extractUserIdFromAuthentication(authentication);
//        logger.debug("Fetching overdue tasks for user: {}", userId);
//
//        Page<TaskResponse> tasks = taskService.getOverdueTasks(userId, pageable);
//        PageResponse<TaskResponse> pageResponse = PageResponse.of(tasks);
//
//        return ResponseEntity.ok(ApiResponse.success("Overdue tasks retrieved successfully", pageResponse));
//    }


    private Long extractUserIdFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }


}
