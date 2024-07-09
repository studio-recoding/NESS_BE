package Ness.Backend.global.error;

import Ness.Backend.global.error.exception.BaseException;
import Ness.Backend.infra.discord.DiscordAlertSender;
import Ness.Backend.infra.discord.DiscordMessageGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final DiscordAlertSender discordAlertSender;
    // 비즈니스 로직 에러 처리
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BaseException baseException, HttpServletRequest httpServletRequest) {
        log.error("handleBusinessException", baseException);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(baseException.getErrorCode(), baseException.getMessage()),null, baseException.getErrorCode().getHttpStatus());
    }

    // 잘못된 API 경로 요청 따로 처리(디스코드 알림에서 제외하기 위함)
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException noResourceFoundException, HttpServletRequest httpServletRequest) {
        log.error("handleNoResourceFoundException", noResourceFoundException);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR), null, INTERNAL_SERVER_ERROR);
    }

    // 잘못된 HTTP 메소드 요청 따로 처리(디스코드 알림에서 제외하기 위함)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException, HttpServletRequest httpServletRequest) {
        log.error("handleMethodNotSupportedException", httpRequestMethodNotSupportedException);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR), null, INTERNAL_SERVER_ERROR);
    }

    // 따로 처리하지 않은 500 에러 모두 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest httpServletRequest) {
        log.error("handleException", exception);
        discordAlertSender.sendDiscordAlarm(exception, httpServletRequest);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR), null, INTERNAL_SERVER_ERROR);
    }
}
