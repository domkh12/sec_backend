package site.secmega.secapi.feature.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.export.ooxml.XlsxWorkbookHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.UserStatus;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.department.DepartmentRepository;
import site.secmega.secapi.feature.message.dto.MessageRequest;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.role.RoleRepository;
import site.secmega.secapi.feature.user.dto.UserFilterRequest;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;
import site.secmega.secapi.feature.user.dto.UserStatsResponse;
import site.secmega.secapi.mapper.UserMapper;
import site.secmega.secapi.util.AuthUtil;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ProductionLineRepository productionLineRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final DepartmentRepository departmentRepository;
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
    public UserStatsResponse getUserStats() {
        Long userId = authUtil.loggedUserId();

        Specification<User> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and((root, query, cb) -> cb.notEqual(root.get("id"), userId));

        long totalUsers = userRepository.count(spec);
        long activeUsers = userRepository.count(spec.and((root, query, cb) -> cb.equal(root.get("status"), UserStatus.Active)));
        long inactiveUsers = userRepository.count(spec.and((root, query, cb) -> cb.equal(root.get("status"), UserStatus.Inactive)));
        long blockedUsers = userRepository.count(spec.and((root, query, cb) -> cb.equal(root.get("status"), UserStatus.Blocked)));

        return UserStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .blockedUsers(blockedUsers)
                .build();
    }

    @Override
    public void blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        user.setStatus(UserStatus.Blocked);
        user.setIsAccountNonLocked(false);
        user.setIsCredentialsNonExpired(false);
        user.setIsAccountNonExpired(false);
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsEnabled(false);
        userRepository.save(user);
        notifyUserUpdate();
    }

    @Override
    public void unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        user.setStatus(UserStatus.Inactive);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonExpired(true);
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsEnabled(true);
        userRepository.save(user);
        notifyUserUpdate();
    }

    @Override
    public void importUsers(MultipartFile file) {
        try (InputStream is = file.getInputStream();

        ){

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to import users");
        }
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

        userMapper.updateUserFromUserRequest(userRequest, user);
        Role role = roleRepository.findById(userRequest.roleId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")
        );
        if (userRequest.departmentId() != null){
            Department department = departmentRepository.findById(userRequest.departmentId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
            );
            user.setDepartment(department);
            if (userRequest.lineId() != null){
                ProductionLine productionLine = productionLineRepository.findById(userRequest.lineId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production line not found!")
                );
                if (!productionLine.getDepartment().getId().equals(department.getId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "The selected production line does not belong to the " + department.getDepartment() + " department.");
                }
                user.setProductionLine(productionLine);
                user.setIsDepartmentHead(false);
            }else {
                user.setIsDepartmentHead(true);
                user.setProductionLine(null);
            }
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

        User user = userMapper.fromUserRequest(userRequest);
        user.setUuid(UUID.randomUUID().toString());
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
        if (userRequest.departmentId() != null){
            Department department = departmentRepository.findById(userRequest.departmentId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
            );
            user.setDepartment(department);
            if (userRequest.lineId() != null){
                ProductionLine productionLine = productionLineRepository.findById(userRequest.lineId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production line not found!")
                );
                if (!productionLine.getDepartment().getId().equals(department.getId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "The selected production line does not belong to the " + department.getDepartment() + " department.");
                }
                user.setProductionLine(productionLine);
                user.setIsDepartmentHead(false);
            }else {
                user.setIsDepartmentHead(true);
                user.setProductionLine(null);
            }
        }
        user.setRoles(new ArrayList<>(List.of(role)));
        User savedUser = userRepository.save(user);
        notifyUserUpdate();
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public Page<UserResponse> findAll(UserFilterRequest userFilterRequest) {
        Long userId = authUtil.loggedUserId();
        Specification<User> spec = Specification.where((root, query, cb) -> cb.conjunction());
        if (userFilterRequest.pageNo() <= 0 || userFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        // Exclude the logged-in user
        spec = spec.and((root, query, cb) -> cb.notEqual(root.get("id"), userId));
        if (userFilterRequest.search() != null){
            String searchTerm = "%" + userFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("employeeId")), searchTerm),
                            cb.like(cb.lower(root.get("nameEn")), searchTerm),
                            cb.like(cb.lower(root.get("nameKh")), searchTerm),
                            cb.like(cb.lower(root.get("phoneNumber")), searchTerm),
                            cb.like(cb.lower(root.get("position")), searchTerm)
                        )
                    );
        }

        if (userFilterRequest.roleId() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("roles").get("id"), userFilterRequest.roleId())
            );
        }

        if (userFilterRequest.departmentId() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("productionLine").get("department").get("id"), userFilterRequest.departmentId())
            );
        }

        if (userFilterRequest.status() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), userFilterRequest.status())
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(userFilterRequest.pageNo() - 1, userFilterRequest.pageSize(), sort);
        Page<User> users = userRepository.findAll(spec, pageRequest);

        return users.map(userMapper::toUserResponse);
    }

    private void notifyUserUpdate() {
        messagingTemplate.convertAndSend("/topic/users", List.of(MessageRequest.builder()
                .isUpdate(true)
                .message("update")
                .build()));
    }
}
