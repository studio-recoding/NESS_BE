package Ness.Backend.global.error;

import Ness.Backend.global.error.exception.BaseException;
import Ness.Backend.infra.discord.DiscordAlertSender;
import Ness.Backend.infra.discord.DiscordMessageGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.ContentCachingRequestWrapper;

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

    // 따로 처리하지 않은 500 에러 모두 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest httpServletRequest) {
        log.error("handleException", exception);
        discordAlertSender.sendDiscordAlarm(exception, httpServletRequest);
        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        return new ResponseEntity<>(ErrorResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR), null, INTERNAL_SERVER_ERROR);
    }
}
