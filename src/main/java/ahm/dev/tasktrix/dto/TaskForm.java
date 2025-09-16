package ahm.dev.tasktrix.dto;

import ahm.dev.tasktrix.domain.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Task creation/update form")
public class TaskForm {
    @Schema(description = "Task title", example = "Learn Spring Boot", required = true)
    private String title;
    
    @Schema(description = "Task description/content", example = "Complete the Spring Boot tutorial")
    private String content;
    
    @Schema(description = "Task status", example = "NEW")
    private TaskStatus status;
}
