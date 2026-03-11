package site.secmega.secapi.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.secmega.secapi.base.UserStatus;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.role.RoleRepository;
import site.secmega.secapi.feature.tv.TvRepository;
import site.secmega.secapi.feature.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class initData {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TvRepository tvRepository;

//    @PostConstruct
    public void init(){
        try {
            initRole();
            initUser();
            initTv();
        } catch (Exception e) {
            System.err.println("Error during initializations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initRole() {
        List<String> roleNames = List.of("ADMIN", "VIEWER", "HR_MANAGER", "OPERATOR", "SUPERVISOR", "QC_INSPECTOR");

        roleNames.forEach(name -> {
            Role role = new Role();
            role.setName(name);
            role.setCreatedAt(LocalDateTime.now());
            roleRepository.save(role);
        });
    }

    private void initUser(){
        Role roleAdmin = roleRepository.findById(1L).orElseThrow();
        Role roleTv = roleRepository.findById(2L).orElseThrow();
        Role roleManager = roleRepository.findById(3L).orElseThrow();
        User user = new User();
        user.setRoles(List.of(roleManager));
        user.setUsername("manager");
        user.setEmployeeId("0011");
        user.setPhoneNumber("0987654321");
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.inactive);
        user.setPassword(passwordEncoder.encode("123"));
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsEnabled(true);
        userRepository.save(user);
        User user1 = new User();
        user1.setRoles(List.of(roleTv));
        user1.setUsername("tv");
        user1.setEmployeeId("0012");
        user1.setPhoneNumber("0987654320");
        user1.setUpdatedAt(LocalDateTime.now());
        user1.setCreatedAt(LocalDateTime.now());
        user1.setPassword(passwordEncoder.encode("123"));
        user.setStatus(UserStatus.inactive);
        user1.setIsAccountNonExpired(true);
        user1.setIsCredentialsNonExpired(true);
        user1.setIsAccountNonLocked(true);
        user1.setIsEnabled(true);
        userRepository.save(user1);
        User user2 = new User();
        user2.setRoles(List.of(roleAdmin));
        user2.setUsername("admin");
        user2.setEmployeeId("0013");
        user2.setPhoneNumber("0987654322");
        user2.setUpdatedAt(LocalDateTime.now());
        user2.setCreatedAt(LocalDateTime.now());
        user2.setStatus(UserStatus.inactive);
        user2.setPassword(passwordEncoder.encode("123"));
        user.setIsAccountNonExpired(true);
        user2.setIsCredentialsNonExpired(true);
        user2.setIsAccountNonLocked(true);
        user2.setIsEnabled(true);
        userRepository.save(user2);
    }

    private void initTv(){
        Tv tv = new Tv();
        tv.setName("General");
        tv.setCreatedAt(LocalDateTime.now());
        tvRepository.save(tv);
        Tv tv1 = new Tv();
        tv1.setName("Sawing1");
        tv1.setCreatedAt(LocalDateTime.now());
        tvRepository.save(tv1);
        Tv tv2 = new Tv();
        tv2.setName("Sawing2");
        tv2.setCreatedAt(LocalDateTime.now());
        tvRepository.save(tv2);
    }

}

