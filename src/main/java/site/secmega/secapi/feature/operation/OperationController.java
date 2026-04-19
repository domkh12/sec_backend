package site.secmega.secapi.feature.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.operation.dto.OperationFilterRequest;
import site.secmega.secapi.feature.operation.dto.OperationResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<OperationResponse> findLookup(){
        return operationService.findLookup();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<OperationResponse> findAll(@ModelAttribute OperationFilterRequest operationFilterRequest){
        return operationService.findAll(operationFilterRequest);
    }

}
