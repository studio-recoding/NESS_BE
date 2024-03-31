package Ness.Backend.domain.auth.security;

import Ness.Backend.global.error.ErrorCode;
import Ness.Backend.global.error.exception.ExpiredTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        log.error("JwtAuthenticationEntryPoint 호출");
        String exception = (String)request.getAttribute("exception");
        response.setCharacterEncoding("utf-8");

        if(exception.equals(ErrorCode.UNAUTHORIZED_ACCESS.getCode())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
        }
        else if(exception.equals(ErrorCode.EXPIRED_TOKEN.getCode())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            ExpiredTokenException expiredTokenException = new ExpiredTokenException();
            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writeValueAsString(expiredTokenException);
            response.getWriter().write(result);
        }
        else if(exception.equals(ErrorCode.UNAUTHORIZED_USER.getCode())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
        }
    }
}