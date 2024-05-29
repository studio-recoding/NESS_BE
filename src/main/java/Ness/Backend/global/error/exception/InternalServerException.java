package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class InternalServerException extends BaseException {
    public InternalServerException() {
        super(ErrorCode._INTERNAL_SERVER_ERROR, ErrorCode._INTERNAL_SERVER_ERROR.getMessage());
    }
    public InternalServerException(String message) {
        super(ErrorCode._INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}