package Ness.Backend.global.fastApi;

import Ness.Backend.domain.profile.email.dto.request.PostFastApiUserEmailDto;
import Ness.Backend.domain.profile.email.dto.response.PostFastApiAiEmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "FastApiEmail",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
/* 오늘 하루 일정이 있는 사용자에게 보여줄 이메일 분석을 위한 API*/
public interface FastApiEmailApi {
    @PostMapping(value = "/email/daily")
    PostFastApiAiEmailDto creatFastApiEmail(PostFastApiUserEmailDto postFastApiUserEmailDto);
}
