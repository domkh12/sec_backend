package site.secmega.secapi.feature.user;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    Page<UserResponse> findAll(Integer pageNo, Integer pageSize);
}
