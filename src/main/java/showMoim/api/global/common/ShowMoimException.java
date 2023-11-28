package showMoim.api.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 소소모임 Custom Exception
 */
@Getter
public class ShowMoimException extends RuntimeException {
    private final String code;
    private HttpStatus httpStatus;

    public ShowMoimException(String code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ShowMoimException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public ShowMoimException(ErrorCode errorCode, HttpStatus httpStatus) {
        this(errorCode);
        this.httpStatus = httpStatus;
    }
}
