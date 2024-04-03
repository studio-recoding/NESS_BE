package Ness.Backend.global.common.response;

import Ness.Backend.global.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import net.minidev.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static <T> CommonResponse<T> postResponse(int code, String message) {
        return CommonResponse.<T>builder()
                .code(code)
                .success(true)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> CommonResponse<T> getResponse(int code, String message, T data) {
        return CommonResponse.<T>builder()
                .code(code)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }


    public static JSONObject jsonOf(ErrorCode errorCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        jsonObject.put("success", false);
        jsonObject.put("message", errorCode.getMessage());
        jsonObject.put("status", errorCode.getHttpStatus().value());
        jsonObject.put("code", errorCode.getCode());
        return jsonObject;
    }

    public static <T> CommonResponse<T> onFailure(ErrorCode errorCode, String message) {
        return CommonResponse.<T>builder()
                .code(errorCode.getHttpStatus().value())
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
