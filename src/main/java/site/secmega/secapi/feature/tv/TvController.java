package site.secmega.secapi.feature.tv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.tv.dto.TvRequest;
import site.secmega.secapi.feature.tv.dto.TvResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tvs")
@RequiredArgsConstructor
public class TvController {

    private final TvService tvService;

    @PreAuthorize("hasAuthority('ROLE_PRODUCTION_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TvResponse createTv(@RequestBody TvRequest tvRequest){
        return tvService.create(tvRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_PRODUCTION_MANAGER', 'ROLE_TV_OPERATOR', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TvResponse> findTv(){
        return tvService.findTv();
    }

}
