package Ness.Backend.global.error.exception;

import Ness.Backend.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundScheduleException extends BaseException {
    public NotFoundScheduleException() {
        super(ErrorCode.NOTFOUND_SCHEDULE, ErrorCode.NOTFOUND_SCHEDULE.getMessage());
    }
    public NotFoundScheduleException(String message) {
        super(ErrorCode.NOTFOUND_SCHEDULE, message);
    }
    public NotFoundScheduleException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}

