package site.secmega.secapi.feature.rack;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.rack.dto.RackFilterRequest;
import site.secmega.secapi.feature.rack.dto.RackRequest;
import site.secmega.secapi.feature.rack.dto.RackResponse;

@RestController
@RequestMapping("/api/v1/racks")
@RequiredArgsConstructor
public class RackController {

    private final RackService rackService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRack(@PathVariable String uuid){
        rackService.deleteRack(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    RackResponse updateRack(@PathVariable String uuid, @Valid @RequestBody RackRequest rackRequest){
        return rackService.updateRack(uuid, rackRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RackResponse createRack(@Valid @RequestBody RackRequest rackRequest){
        return rackService.createRack(rackRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<RackResponse> findAll(@ModelAttribute RackFilterRequest rackFilterRequest){
        return rackService.findAll(rackFilterRequest);
    }

}
