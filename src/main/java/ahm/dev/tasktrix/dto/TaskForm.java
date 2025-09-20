package ahm.dev.tasktrix.dto;

import ahm.dev.tasktrix.domain.TaskStatus;
import lombok.Data;

@Data
public class TaskForm {
    private String title;
    private String content;
    private TaskStatus status;
}
