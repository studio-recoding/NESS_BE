package Ness.Backend.global.fastApi;


import Ness.Backend.domain.report.dto.request.PostFastApiUserRecommendDto;
import Ness.Backend.domain.report.dto.response.PostFastApiAiRecommendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(
        name = "FastApiRecommend",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiRecommendApi {
    @PostMapping(value = "/recommendation/main")
    PostFastApiAiRecommendDto creatFastApiRecommend(PostFastApiUserRecommendDto postFastApiUserRecommendDto);
}