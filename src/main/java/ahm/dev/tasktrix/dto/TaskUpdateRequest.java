package ahm.dev.tasktrix.dto;

import ahm.dev.tasktrix.domain.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
// import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

// @Schema(description = "Task update request")
public record TaskUpdateRequest(
    // @Schema(description = "Task title", example = "Complete project documentation")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    String title,

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String content,

    // @Schema(description = "Task status", example = "IN_PROGRESS")
    TaskStatus status,

    // @Schema(description = "Due date", example = "2024-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dueDate
) {}