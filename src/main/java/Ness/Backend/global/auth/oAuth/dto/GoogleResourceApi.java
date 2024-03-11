package Ness.Backend.global.auth.oAuth.dto;

import Ness.Backend.domain.auth.oAuth.dto.GoogleResourceDto;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "GoogleResource",
        url = "https://www.googleapis.com")
public interface GoogleResourceApi {
    @PostMapping(
            value = "/oauth2/v2/userinfo",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Authorization: {authorization}")
    GoogleResourceDto googleGetResource(@Param("authorization") String accessToken);
}
