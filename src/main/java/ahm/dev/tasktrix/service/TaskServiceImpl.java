package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.TaskForm;
import ahm.dev.tasktrix.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(TaskForm taskform, User user) {
        if(taskform == null) throw new IllegalArgumentException("Task cannot be null");
        Task task = Task.builder()
                .title(taskform.getTitle())
                .content(taskform.getContent())
                .status(taskform.getStatus())
                .user(user)
                .build();
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(User user,Long id, Task taskupdate) {
        Task task = taskRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new IllegalArgumentException("Task not found or unauthorized")
        );
        if(taskupdate.getTitle() != null) task.setTitle(taskupdate.getTitle());
        if(taskupdate.getContent() != null) task.setContent(taskupdate.getContent());
        if(taskupdate.getStatus() != null) task.setStatus(taskupdate.getStatus());
        return taskRepository.save(task);
    }


    @Override
    public Task getTaskById(User user, Long taskId) {
        return taskRepository.findByIdAndUser(taskId, user).orElseThrow(
                () -> new IllegalArgumentException("Task not found")
        );
    }

    @Override
    public List<Task> getAllTasks(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    @Override
    public void deleteTask(User user, Long taskId) {
        taskRepository.findByIdAndUser(taskId, user)
                .ifPresentOrElse(taskRepository::delete, () -> {
                    throw new RuntimeException("Task not found or unauthorized");
                });
    }

}
