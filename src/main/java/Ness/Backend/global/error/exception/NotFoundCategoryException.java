package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;
@Getter
public class NotFoundCategoryException extends BaseException {
    public NotFoundCategoryException() {
        super(ErrorCode.NOTFOUND_CATEGORY, ErrorCode.NOTFOUND_CATEGORY.getMessage());
    }
    public NotFoundCategoryException(String message) {
        super(ErrorCode.NOTFOUND_CATEGORY, message);
    }
    public NotFoundCategoryException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
