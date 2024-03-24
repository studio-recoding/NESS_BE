package Ness.Backend.global.auth.oAuth.naver;

import Ness.Backend.domain.auth.oAuth.naver.dto.NaverTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "NaverOAuth",
        url = "https://nid.naver.com")
public interface NaverOAuthApi {
    @PostMapping(
            value = "/oauth2.0/token?" +
                    "code={CODE}" +
                    "&client_id={CLIENT_ID}" +
                    "&client_secret={CLIENT_SECRET}" +
                    "&redirect_uri={REDIRECT_URI}" +
                    "&grant_type={GRANT_TYPE}" +
                    "&state={STATE}")
    NaverTokenDto naverGetToken(
            @PathVariable("CODE") String code,
            @PathVariable("CLIENT_ID") String clientId,
            @PathVariable("CLIENT_SECRET") String clientSecret,
            @PathVariable("REDIRECT_URI") String redirectUri,
            @PathVariable("GRANT_TYPE") String grantType,
            @PathVariable("STATE") String state);
}
