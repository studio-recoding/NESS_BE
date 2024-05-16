package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.auth.jwt.entity.JwtToken;
import Ness.Backend.domain.auth.security.AuthDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/*OAuth 인증에 성공하면 시큐리티가 접근 시켜주는 클래스*/
@Slf4j
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

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
        /* 프론트 개발 환경 */
        if (url.equals("api.nessplanning.com")) {
            redirect_url = "http://localhost:3000/oauth/google/success/ing";
        }
        //TODO: 프론트 프로덕션 환경만 지원하는 백엔드 구축
        
        return redirect_url;
    }
}
