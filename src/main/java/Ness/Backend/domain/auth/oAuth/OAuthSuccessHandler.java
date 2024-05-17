package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.auth.jwt.entity.JwtToken;
import Ness.Backend.domain.auth.security.AuthDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/*OAuth 인증에 성공하면 시큐리티가 접근 시켜주는 클래스*/
@Slf4j
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Value("frontend.redirect-url")
    private String frontRedirectUrl;

    @Value("backend.server-name")
    private String backServerName;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        /*인증에 성공한 사용자*/
        AuthDetails oAuth2User = (AuthDetails) authentication.getPrincipal();

        /*JwtToken 생성*/
        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(oAuth2User.getUsername());

        //TODO: RefreshToken update
        //refreshTokenService.saveRefreshToken(jwtToken.getJwtRefreshToken(),oAuth2User.getUser().getAuthKey());

        /*JwtToken과 함께 리다이렉트*/
        String targetUrl = UriComponentsBuilder.fromUriString(setRedirectUrl(request.getServerName()))
                .queryParam("jwtAccessToken", jwtToken.getJwtAccessToken())
                .queryParam("jwtRefreshToken", jwtToken.getJwtRefreshToken())
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String setRedirectUrl(String url) {
        String redirect_url = null;

        /* 백엔드 개발 환경 */
        if (url.equals("localhost")) {
            redirect_url = "http://localhost:8080/oauth/google/success";
        }
        /* 프론트 개발 또는 프로덕션 환경 */
        else {
            log.info("backServerName: " + backServerName);
            log.info("url: " + url);
            log.info("frontRedirectUrl: " + frontRedirectUrl);
            redirect_url = frontRedirectUrl + "/oauth/google/success/ing";
        }
        return redirect_url;
    }
}
