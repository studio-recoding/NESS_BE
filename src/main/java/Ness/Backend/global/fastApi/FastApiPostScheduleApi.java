package Ness.Backend.global.fastApi;

import Ness.Backend.domain.schedule.dto.request.PostFastApiScheduleDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "FastApiSchedule",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiPostScheduleApi {
    @PostMapping(value = "/chromadb/add_schedule")
    ResponseEntity<JsonNode> creatFastApiSchedule(PostFastApiScheduleDto postFastApiScheduleDto);
}
