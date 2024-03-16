package Ness.Backend.global.auth.oAuth.kakao;

import Ness.Backend.domain.auth.oAuth.kakao.dto.KakaoTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "KakaoOAuth",
        url = "https://kauth.kakao.com")
public interface KakaoOAuthApi {
    @PostMapping(
            value = "/oauth/token?" +
                    "code={CODE}" +
                    "&client_id={CLIENT_ID}" +
                    "&redirect_uri={REDIRECT_URI}" +
                    "&grant_type={GRANT_TYPE}")
    KakaoTokenDto kakaoGetToken(
            @PathVariable("CODE") String code,
            @PathVariable("CLIENT_ID") String clientId,
            @PathVariable("REDIRECT_URI") String redirectUri,
            @PathVariable("GRANT_TYPE") String grantType);
}
