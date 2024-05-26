package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;
@Getter
public class DefaultCategoryException extends BaseException {
    public DefaultCategoryException() {
        super(ErrorCode.INVALID_CATEGORY_DELETE, ErrorCode.INVALID_CATEGORY_DELETE.getMessage());
    }
    public DefaultCategoryException(String message) {
        super(ErrorCode.INVALID_CATEGORY_DELETE, message);
    }
    public DefaultCategoryException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
