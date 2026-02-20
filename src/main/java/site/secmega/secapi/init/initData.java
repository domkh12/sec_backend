package site.secmega.secapi.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.role.RoleRepository;
import site.secmega.secapi.feature.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class initData {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    public void init(){
        try {
            initRole();
            initUser();
        } catch (Exception e) {
            System.err.println("Error during initializations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initRole() {
        List<String> roleNames = List.of("ADMIN", "TV_OPERATOR", "PRODUCTION_MANAGER");

        roleNames.forEach(name -> {
            Role role = new Role();
            role.setName(name);
            role.setCreatedAt(LocalDateTime.now());
            roleRepository.save(role);
        });
    }

    private void initUser(){
        Role role = roleRepository.findById(3L).orElseThrow();
        User user = new User();
        user.setRoles(List.of(role));
        user.setUsername("manager");
        user.setEmployee_id("0011");
        user.setPhoneNumber(987654321);
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode("123"));
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsEnabled(true);
        userRepository.save(user);
    }


}
