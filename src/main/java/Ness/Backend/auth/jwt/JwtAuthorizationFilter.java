package Ness.Backend.auth.jwt;

import Ness.Backend.auth.AuthDetailService;
import Ness.Backend.auth.AuthDetails;
import Ness.Backend.domain.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/* 사용자의 권한 부여
 * 요청에 포함된 JWT 토큰을 검증하고, 토큰에서 추출한 권한 정보를 기반으로 사용자에 대한 권한을 확인
 * 모든 사용자가 모든 리소스에 대한 권한을 가지는 것은 아님-특정 리소스에 대한 권한만 가지도록 해야 함*/
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtTokenProvider jwtTokenProvider;

    private AuthDetailService authDetailService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AuthDetailService authDetailService) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.authDetailService = authDetailService;
    }

    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 통과한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        /* 헤더 추출 및 정상적인 헤더인지 확인 */
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        /* 헤더 안의 JWT 토큰을 검증해 정상적인 사용자인지 확인 */
        String jwtToken = jwtHeader.substring(7);
        Member tokenMember = jwtTokenProvider.validJwtToken(jwtToken);

        if(tokenMember != null){ //토큰이 정상일 경우
            AuthDetails authDetails = new AuthDetails(tokenMember);

            /* JWT 토큰 서명이 정상이면 Authentication 객체 생성 */
            Authentication authentication = new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities());

            /* 시큐리티 세션에 Authentication 을 저장 */
            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}