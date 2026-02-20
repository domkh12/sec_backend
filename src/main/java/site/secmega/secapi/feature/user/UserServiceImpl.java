package site.secmega.secapi.feature.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.role.RoleRepository;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;
import site.secmega.secapi.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        if (userRepository.existsByUsername(userRequest.username())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist");
        }

        if (userRepository.existsByPhoneNumber(userRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }

        if (userRepository.existsByEmployee_id(userRequest.employee_id())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee ID already exist");
        }

        User user = userMapper.fromUserRequest(userRequest);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(true);
        Role role = roleRepository.findById(userRequest.roleId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")
        );
        user.setRoles(List.of(role));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}
