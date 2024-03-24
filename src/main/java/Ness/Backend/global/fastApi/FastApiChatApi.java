package Ness.Backend.global.fastApi;

import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(
        name = "FastApiChat",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiChatApi {
    @PostMapping(value = "/chromadb/add_schedule")
    ResponseEntity<JsonNode> creatFastApiChat(PostFastApiUserChatDto postFastApiUserChatDto);
}
