package site.secmega.secapi.feature.user;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.user.dto.*;

import java.util.List;

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

    List<UserLookupResponse> getUserLookup();
}
