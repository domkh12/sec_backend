package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromUserRequest(UserRequest userRequest);
    UserResponse toUserResponse(User user);
}
