package showMoim.api.global.common;

import lombok.*;

/**
 * 공통 API 응답형식 - { status, message, data }
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    public static final String SUCCESS_STATUS = "success";
    public static final String ERROR_STATUS = "error";

    private String status;

    private T data;
    private String message;

    public static <T> ApiResponse<T> of(String status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    private ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}