package Ness.Backend.global.auth.oAuth.google;

import Ness.Backend.domain.auth.oAuth.google.dto.GoogleResourceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "GoogleResource",
        url = "https://www.googleapis.com")
public interface GoogleResourceApi {
    @GetMapping(
            value = "/oauth2/v2/userinfo",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleResourceDto googleGetResource(@RequestHeader("Authorization") String accessToken);
}
