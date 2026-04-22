package site.secmega.secapi.feature.user;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.user.dto.UserFilterRequest;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;
import site.secmega.secapi.feature.user.dto.UserStatsResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    Page<UserResponse> findAll(UserFilterRequest userFilterRequest);

    void setActive(Long id);

    void setInactive(Long id);

    UserResponse updateUser(Long id, @Valid UserRequest userRequest);

    void deleteUser(Long id);

    UserStatsResponse getUserStats();

    void blockUser(Long id);

    void unblockUser(Long id);

    void importUsers(MultipartFile file);
}
