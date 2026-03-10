package site.secmega.secapi.feature.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.secmega.secapi.feature.file.dto.FileResponse;
import site.secmega.secapi.util.FileUtil;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PreAuthorize("hasAnyAuthority('ROLE_HR_MANAGER', 'ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    FileResponse uploadFile(@RequestPart MultipartFile file) throws Exception{
        return fileService.uploadFile(file);
    }

}
