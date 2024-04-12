package Ness.Backend.global.fastApi;

import Ness.Backend.domain.report.dto.request.PostFastApiUserMemoryDto;
import Ness.Backend.domain.report.dto.response.PostFastApiAiMemoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "FastApiMemory",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiMemoryApi {
    @PostMapping(value = "/report/memory_emoji")
    PostFastApiAiMemoryDto creatFastApiMemory(PostFastApiUserMemoryDto postFastApiUserMemoryDto);
}
