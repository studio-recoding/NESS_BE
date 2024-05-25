package Ness.Backend.global.fastApi;

import Ness.Backend.domain.schedule.dto.request.DeleteFastApiScheduleDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
@FeignClient(
        name = "FastApiDeleteSchedule",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiDeleteScheduleApi {
    @DeleteMapping(value = "/chromadb/delete_schedule")
    ResponseEntity<JsonNode> deleteFastApiSchedule(DeleteFastApiScheduleDto deleteFastApiScheduleDto);
}