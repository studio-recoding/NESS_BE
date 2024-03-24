package Ness.Backend.global.fastApi;

import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.response.PostFastApiAiChatDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(
        name = "FastApiChat",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiChatApi {
    @PostMapping(value = "/chat/case")
    PostFastApiAiChatDto creatFastApiChat(PostFastApiUserChatDto postFastApiUserChatDto);
}
