package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

/**
 * status : 401
 */
@Getter
public class UnauthorizedAccessException extends BaseException {
    public UnauthorizedAccessException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS, ErrorCode.UNAUTHORIZED_ACCESS.getMessage());
    }
    public UnauthorizedAccessException(String message) {
        super(ErrorCode.UNAUTHORIZED_ACCESS, message);
    }
    public UnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
