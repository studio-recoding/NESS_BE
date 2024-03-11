package Ness.Backend.domain.auth.jwt;

import Ness.Backend.domain.auth.inmemory.RefreshTokenService;
import Ness.Backend.domain.auth.jwt.entity.JwtToken;
import Ness.Backend.domain.auth.security.AuthDetails;
import Ness.Backend.domain.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/* 사용자 인증(확인)
 * 사용자의 로그인 시도를 가로채서 JWT 토큰을 생성하고, 성공적으로 로그인이 완료되면 생성된 토큰을 응답 헤더에 추가 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /* 사용자의 로그인 시도를 가로채는 attemptAuthentication
     * 인증 객체(Authentication)을 만들기 시도 */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper(); //JSON 데이터를 Java 객체로 매핑
            Member member = om.readValue(request.getInputStream(), Member.class); //클라이언트에서 전달된 JSON 데이터는 Member 클래스로 변환
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    member.getEmail(),
                    member.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
            return authentication;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not set user authentication in security context", e);
        }

        return null;
    }

    /* attemptAuthentication 메소드가 호출 된 후, response에 JWT 토큰을 담아서 전송 */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        AuthDetails authDetails = (AuthDetails) authResult.getPrincipal();

        String authEmail = authDetails.getMember().getEmail();

        /* JwtToken 생성(accessToken, refreshToken) */
        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(authEmail);

        /* RefreshToken 업데이트(email로 authKey 설정) */
        refreshTokenService.saveRefreshToken(jwtToken.getJwtRefreshToken(), authDetails.getMember().getEmail());

        /* 가장 흔한 방식인 Bearer Token을 사용해 응답 */
        response.addHeader("Authorization", "Bearer " + jwtToken.getJwtAccessToken());
        response.addHeader("Refresh-Token", "Bearer " + jwtToken.getJwtRefreshToken());

        //TODO: JwtToken 과 함께 리다이렉트
        /*
        String targetUrl = UriComponentsBuilder.fromUriString(setRedirectUrl(request.getServerName()))
                .queryParam("jwtAccessToken", jwtToken.getJwtAccessToken())
                .queryParam("jwtRefreshToken", jwtToken.getJwtRefreshToken())
                .build().toUriString();
         */
    }

    /*
    private String setRedirectUrl(String url) {
        String redirect_url = null;
        if (url.equals("localhost")) {
            redirect_url = "http://localhost:8080/oauth/google/success";
        }
        if (url.equals("ness.site")) {
            redirect_url = "http://localhost:3000/oauth/google/success/ing";
        }
        if (url.equals("ness.com")) {
            redirect_url = "https://www.teampple.com/oauth/google/success/ing";
        }
        return redirect_url;
    }
     */
}