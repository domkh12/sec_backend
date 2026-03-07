package site.secmega.secapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiException {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        System.out.println(e.getStatusCode());
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("unauthorized");
            ErrorDetailResponse<?> errorDetailResponse = ErrorDetailResponse.builder()
                    .code(e.getStatusCode().value())
                    .description("Session Expired")
                    .build();
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error(errorDetailResponse)
                    .build(), e.getStatusCode());
        }

        ErrorDetailResponse<?> errorDetailResponse = ErrorDetailResponse.builder()
                .code(e.getStatusCode())
                .description(e.getReason())
                .build();

        return new ResponseEntity<>(ErrorResponse.builder()
                .error(errorDetailResponse)
                .build(), e.getStatusCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorResponse<?> handleErrorResponse(MethodArgumentNotValidException e) {
        List<Map<String,String>> errorDetail = new ArrayList<>();

        e.getFieldErrors().forEach(
                error ->{
                    Map<String, String> errorDetailMap = new HashMap<>();
                    errorDetailMap.put("reason", error.getDefaultMessage());
                    errorDetailMap.put("field", error.getField());
                    errorDetail.add(errorDetailMap);
                }
        );

        ErrorDetailResponse<?> errorDetailResponse = ErrorDetailResponse.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .description(errorDetail)
                .build();

        return ErrorResponse.builder()
                .error(errorDetailResponse)
                .build();
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> handleGenericException(Exception e) {
        Map<String, String> detail = new HashMap<>();
        detail.put("message", e.getMessage() != null ? e.getMessage() : "Unexpected error occurred");
        detail.put("type", e.getClass().getSimpleName());
        detail.put("cause", e.getCause() != null ? e.getCause().getMessage() : "Unknown");

        ErrorDetailResponse<?> errorDetailResponse = ErrorDetailResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .description(detail)
                .build();

        return new ResponseEntity<>(ErrorResponse.builder()
                .error(errorDetailResponse)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
