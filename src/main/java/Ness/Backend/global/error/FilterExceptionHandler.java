package Ness.Backend.global.error;

import Ness.Backend.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterExceptionHandler {
    public static void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(ErrorResponse.jsonOf(errorCode));
    }
}