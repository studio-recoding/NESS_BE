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
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
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
        if (url.equals("localhost")) {
            redirect_url = "http://localhost:3000/login/oauth/google";
        }
        if (url.equals("nessplanning.com")) {
            redirect_url = "https://www.nessplanning.com/login/oauth/google";
        }

        return redirect_url;
    }
}
