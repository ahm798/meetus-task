package ahm.dev.tasktrix.mapper;

import ahm.dev.tasktrix.domain.Task;
import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.TaskCreateRequest;
import ahm.dev.tasktrix.dto.TaskResponse;
import ahm.dev.tasktrix.dto.TaskUpdateRequest;
import org.mapstruct.*;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "completedAt", ignore = true)
    Task toEntity(TaskCreateRequest request, @Context User user);

    @AfterMapping
    default void setUser(@MappingTarget Task task, @Context User user) {
        task.setUser(user);
    }

    @Mapping(target = "isOverdue", expression = "java(task.isOverdue())")
    TaskResponse toResponse(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TaskUpdateRequest request, @MappingTarget Task task);
}
