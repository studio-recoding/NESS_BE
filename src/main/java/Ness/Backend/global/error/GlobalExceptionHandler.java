package Ness.Backend.global.error;

import Ness.Backend.global.error.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 비즈니스 로직 에러 처리
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BaseException baseException, HttpServletRequest httpServletRequest) {
        log.error("handleBusinessException", baseException);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(baseException.getErrorCode()),null, baseException.getErrorCode().getHttpStatus());
    }
}
