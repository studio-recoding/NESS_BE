package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {
    public NotFoundException() {
        super(ErrorCode._BAD_REQUEST, ErrorCode._BAD_REQUEST.getMessage());
    }
    public NotFoundException(String message) {
        super(ErrorCode._BAD_REQUEST, message);
    }
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
