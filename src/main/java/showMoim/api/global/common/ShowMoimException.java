package showMoim.api.global.common;

import lombok.Getter;

/**
 * 소소모임 Custom Exception
 */
@Getter
public class ShowMoimException extends RuntimeException {
    private final String code;

    public ShowMoimException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ShowMoimException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
}
