package Ness.Backend.global.response;

import Ness.Backend.global.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class ApiResponse<T> {
    @JsonProperty("status")
    private int code;

    @JsonProperty("isSuccess")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @Builder
    public ApiResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> postResponse(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .success(true)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> getResponse(int code, String message, T data) {
        return ApiResponse.<T>builder()
                .code(code)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }


    public static <T> ApiResponse<T> onFailure(ErrorCode errorCode, String message) {
        return ApiResponse.<T>builder()
                .code(errorCode.getHttpStatus().value())
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
