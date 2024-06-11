package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;
@Getter
public class OAuthVerificationException extends BaseException {
    public OAuthVerificationException() {
        super(ErrorCode.OAUTH_ERROR, ErrorCode.OAUTH_ERROR.getMessage());
    }
    public OAuthVerificationException(String message) {
        super(ErrorCode.OAUTH_ERROR, message);
    }
    public OAuthVerificationException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
