package Ness.Backend.global.auth.oAuth.google;

import Ness.Backend.domain.auth.oAuth.google.dto.GoogleTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "GoogleOAuth",
        url = "https://oauth2.googleapis.com")
public interface GoogleOAuthApi {
    @PostMapping(
            value = "/token?" +
                    "code={CODE}" +
                    "&client_id={CLIENT_ID}" +
                    "&client_secret={CLIENT_SECRET}" +
                    "&redirect_uri={REDIRECT_URI}" +
                    "&grant_type={GRANT_TYPE}",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenDto googleGetToken(
            @PathVariable("CODE") String code,
            @PathVariable("CLIENT_ID") String clientId,
            @PathVariable("CLIENT_SECRET") String clientSecret,
            @PathVariable("REDIRECT_URI") String redirectUri,
            @PathVariable("GRANT_TYPE") String grantType);
}
