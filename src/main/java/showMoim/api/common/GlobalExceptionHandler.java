package showMoim.api.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 예외 발생 시 공통 Api Response 형태로 응답하기 위한 Advice
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleException(ShowMoimException e) {
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.of(ApiResponse.ERROR_STATUS, e.getMessage(), e.getCode()));
    }
}
