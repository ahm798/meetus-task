package ahm.dev.tasktrix.mapper;


import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.SignupRequest;
import ahm.dev.tasktrix.dto.AuthResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "role", constant = "USER")
    User toEntity(SignupRequest request);

    AuthResponse.UserInfo toUserInfo(User user);
}