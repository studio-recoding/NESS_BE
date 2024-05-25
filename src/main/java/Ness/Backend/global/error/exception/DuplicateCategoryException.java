package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateCategoryException extends BaseException {
    public DuplicateCategoryException() {
        super(ErrorCode.INVALID_CATEGORY_NAME, ErrorCode.INVALID_CATEGORY_NAME.getMessage());
    }
    public DuplicateCategoryException(String message) {
        super(ErrorCode.INVALID_CATEGORY_NAME, message);
    }
    public DuplicateCategoryException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
