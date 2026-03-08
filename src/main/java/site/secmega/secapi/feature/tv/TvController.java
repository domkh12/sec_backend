package site.secmega.secapi.feature.tv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.tv.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tvs")
@RequiredArgsConstructor
public class TvController {

    private final TvService tvService;

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_ADMIN', 'ROLE_VIEWER')")
    @GetMapping("/tv-general")
    @ResponseStatus(HttpStatus.OK)
    List<TvGeneralResponse> getTvGeneralData(){
        return tvService.getTvGeneralData();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/data/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    TvDataResponse createDataTv(@PathVariable String name){
        return tvService.createDataTv(name);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/data")
    @ResponseStatus(HttpStatus.CREATED)
    TvDataResponse updateDataTv(@RequestBody TvDataRequest tvDataRequest){
        return tvService.updateDataTv(tvDataRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_VIEWER', 'ROLE_ADMIN')")
    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    TvDataResponse getDataByTvName(@PathVariable String name){
        return tvService.getDataByTvName(name);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TvResponse createTv(@RequestBody TvRequest tvRequest){
        return tvService.create(tvRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_VIEWER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TvResponse> findTv(){
        return tvService.findTv();
    }

}
