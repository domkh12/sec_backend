package site.secmega.secapi.feature.rack;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.rack.dto.RackFilterRequest;
import site.secmega.secapi.feature.rack.dto.RackRequest;
import site.secmega.secapi.feature.rack.dto.RackResponse;

public interface RackService {
    void deleteRack(String uuid);

    RackResponse updateRack(String uuid, @Valid RackRequest rackRequest);

    RackResponse createRack(@Valid RackRequest rackRequest);

    Page<RackResponse> findAll(RackFilterRequest rackFilterRequest);
}
