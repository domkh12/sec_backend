package site.secmega.secapi.feature.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.user.dto.UserFilterRequest;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;
import site.secmega.secapi.feature.user.dto.UserStatsResponse;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void importUsers(@RequestParam("file") MultipartFile file){
        userService.importUsers(file);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}/unblock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unblockUser(@PathVariable Long id){
        userService.unblockUser(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/{id}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void blockUser(@PathVariable Long id){
        userService.blockUser(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    UserStatsResponse getUserStats(){
        return userService.getUserStats();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse updateUser(@PathVariable Long id,@Valid @RequestBody UserRequest userRequest){
        return userService.updateUser(id, userRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<UserResponse> findAll(@ModelAttribute UserFilterRequest userFilterRequest){
        return userService.findAll(userFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse createUser(@Valid @RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    @PatchMapping(value = "/{id}/inactive",
            consumes ={
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.TEXT_PLAIN_VALUE,
                    "*/*"   // 👈 needed for fetch keepalive
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void setInactive(@PathVariable Long id){
        userService.setInactive(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    @PatchMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void setActive(@PathVariable Long id){
        userService.setActive(id);
    }

}
