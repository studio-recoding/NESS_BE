package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

/**
 * status : 403
 */
@Getter
public class ExpiredTokenException extends BaseException {
    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN, ErrorCode.EXPIRED_TOKEN.getMessage());
    }
    public ExpiredTokenException(String message) {
        super(ErrorCode.EXPIRED_TOKEN, message);
    }
    public ExpiredTokenException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
