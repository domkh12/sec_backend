package site.secmega.secapi.feature.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.UserStatus;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.message.dto.MessageRequest;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.role.RoleRepository;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;
import site.secmega.secapi.mapper.UserMapper;
import site.secmega.secapi.util.AuthUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ProductionLineRepository productionLineRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        if (userRepository.existsByUsernameIgnoreCaseAndIdNotAndDeletedAtIsNull(userRequest.username(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist");
        }

        if (userRepository.existsByPhoneNumberAndIdNotAndDeletedAtIsNull(userRequest.phoneNumber(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }

        if (userRepository.existsByEmployeeIdAndIdNotAndDeletedAtIsNull(userRequest.employeeId(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee ID already exist");
        }

        if (userRequest.email() != null && userRepository.existsByEmailIgnoreCaseAndIdNotAndDeletedAtIsNull(userRequest.email(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exist");
        }

        userMapper.updateUserFromUserRequest(userRequest, user);
        Role role = roleRepository.findById(userRequest.roleId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")
        );
        if (userRequest.lineId() != null){
            ProductionLine productionLine = productionLineRepository.findById(userRequest.lineId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production line not found")
            );
            user.setProductionLine(productionLine);
        }

        user.setRoles(new ArrayList<>(List.of(role)));
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        notifyUserUpdate();
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public void setInactive(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        user.setStatus(UserStatus.Inactive);
        userRepository.save(user);
        notifyUserUpdate();
    }

    @Override
    public void setActive(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        user.setStatus(UserStatus.Active);
        userRepository.save(user);
        notifyUserUpdate();
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtIsNull(userRequest.username())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exist");
        }

        if (userRepository.existsByPhoneNumberAndDeletedAtIsNull(userRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }

        if (userRepository.existsByEmployeeIdAndDeletedAtIsNull(userRequest.employeeId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee ID already exist");
        }

        if (userRequest.email() != null && userRepository.existsByEmailIgnoreCaseAndDeletedAtIsNull(userRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exist");
        }

        User user = userMapper.fromUserRequest(userRequest);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(true);
        user.setStatus(UserStatus.Inactive);
        Role role = roleRepository.findById(userRequest.roleId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")
        );
        if (userRequest.lineId() != null){
            ProductionLine productionLine = productionLineRepository.findById(userRequest.lineId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production line not found!")
            );
            user.setProductionLine(productionLine);
        }
        user.setRoles(new ArrayList<>(List.of(role)));
        User savedUser = userRepository.save(user);
        notifyUserUpdate();
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public Page<UserResponse> findAll(Integer pageNo, Integer pageSize) {
        Long userId = authUtil.loggedUserId();
        if (pageNo <= 0 || pageSize <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<User> users = userRepository.findByIdNot(userId, pageRequest);

        return users.map(userMapper::toUserResponse);
    }

    private void notifyUserUpdate() {
        messagingTemplate.convertAndSend("/topic/users", List.of(MessageRequest.builder()
                .isUpdate(true)
                .message("update")
                .build()));
    }
}
