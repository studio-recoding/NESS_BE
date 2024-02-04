package Ness.Backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class CommonResponse<T> {
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
    public CommonResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResponse<T> postSuccess(int code, String message) {
        return CommonResponse.<T>builder()
                .code(code)
                .success(true)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> CommonResponse<T> getSuccess(int code, String message, T data) {
        return CommonResponse.<T>builder()
                .code(code)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}
