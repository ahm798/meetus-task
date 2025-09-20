package ahm.dev.tasktrix.controller;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.TaskForm;
import ahm.dev.tasktrix.service.TaskService;
import ahm.dev.tasktrix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskForm task) {
        return ResponseEntity.ok(taskService.createTask(task, getCurrentUser()));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks(getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(getCurrentUser(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(getCurrentUser(),id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(getCurrentUser(), id);
        return ResponseEntity.noContent().build();
    }
}
