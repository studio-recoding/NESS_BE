package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

/**
 * status : 403
 */
@Getter
public class UnauthorizedUserException extends BaseException {
    public UnauthorizedUserException() {
        super(ErrorCode.UNAUTHORIZED_USER, ErrorCode.UNAUTHORIZED_USER.getMessage());
    }
    public UnauthorizedUserException(String message) {
        super(ErrorCode.UNAUTHORIZED_USER, message);
    }
    public UnauthorizedUserException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
