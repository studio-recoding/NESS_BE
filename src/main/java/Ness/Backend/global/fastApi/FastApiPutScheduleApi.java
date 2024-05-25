package Ness.Backend.global.fastApi;

import Ness.Backend.domain.schedule.dto.request.PutFastApiScheduleDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(
        name = "FastApiPutSchedule",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiPutScheduleApi {
    @PutMapping(value = "/chromadb/update_schedule")
    ResponseEntity<JsonNode> putFastApiSchedule(PutFastApiScheduleDto putFastApiScheduleDto);
}
