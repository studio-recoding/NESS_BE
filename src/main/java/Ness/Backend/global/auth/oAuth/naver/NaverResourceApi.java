package Ness.Backend.global.auth.oAuth.naver;

import Ness.Backend.domain.auth.oAuth.kakao.dto.KakaoResourceDto;
import Ness.Backend.domain.auth.oAuth.naver.dto.NaverResourceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "NaverResource",
        url = "https://openapi.naver.com")
public interface NaverResourceApi {
    @GetMapping(
            value = "/v1/nid/me")
    NaverResourceDto naverGetResource(@RequestHeader("Authorization") String accessToken);
}