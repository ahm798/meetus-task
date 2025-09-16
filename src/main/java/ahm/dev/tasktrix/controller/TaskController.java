package ahm.dev.tasktrix.controller;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.TaskForm;
import ahm.dev.tasktrix.service.TaskService;
import ahm.dev.tasktrix.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
@Tag(name = "Task Management", description = "CRUD operations for task management")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Operation(summary = "Create new task", description = "Create a new task for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task creation details",
                    content = @Content(schema = @Schema(implementation = TaskForm.class),
                            examples = @ExampleObject(value = "{\"title\":\"Learn Spring Boot\",\"content\":\"Complete tutorial\",\"status\":\"NEW\"}")))
            @RequestBody TaskForm task) {
        return ResponseEntity.ok(taskService.createTask(task, getCurrentUser()));
    }

    @Operation(summary = "Get all user tasks", description = "Retrieve all tasks belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks(getCurrentUser()));
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID (user can only access their own tasks)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "403", description = "Access denied - task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(getCurrentUser(), id));
    }

    @Operation(summary = "Update task", description = "Update an existing task (user can only update their own tasks)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "403", description = "Access denied - task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated task details",
                    content = @Content(schema = @Schema(implementation = Task.class),
                            examples = @ExampleObject(value = "{\"title\":\"Updated Task\",\"content\":\"Updated content\",\"status\":\"COMPLETED\"}")))
            @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(getCurrentUser(),id, task));
    }

    @Operation(summary = "Delete task", description = "Delete a task by ID (user can only delete their own tasks)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id) {
        taskService.deleteTask(getCurrentUser(), id);
        return ResponseEntity.noContent().build();
    }
}
