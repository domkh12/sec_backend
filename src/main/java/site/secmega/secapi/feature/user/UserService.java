package site.secmega.secapi.feature.user;

import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
}
