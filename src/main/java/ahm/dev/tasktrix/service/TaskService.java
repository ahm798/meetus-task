package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.TaskForm;

import java.util.List;

public interface TaskService {
    Task createTask(TaskForm taskform, User user);
    Task updateTask(User user,Long taskId, Task task);
    Task getTaskById(User user, Long taskId);
    List<Task> getAllTasks(User user);
    void deleteTask(User user,Long taskId);
}
