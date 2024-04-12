package Ness.Backend.global.fastApi;

import Ness.Backend.domain.report.dto.request.PostFastApiUserTagDto;
import Ness.Backend.domain.report.dto.response.PostFastApiAiTagDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "FastApiTag",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiTagApi {
    @PostMapping(value = "/report/tags")
    PostFastApiAiTagDto creatFastApiTag(PostFastApiUserTagDto postFastApiUserTagDto);
}