package site.secmega.secapi.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.feature.role.RoleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class initData {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init(){
        try {
            initRole();
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

}
