package Ness.Backend.global.fastApi;

import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.response.PostFastApiAiChatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(
        name = "FastApiChat",
        url = "${spring.cloud.openfeign.client.config.fastapi.url}")
public interface FastApiChatApi {
    @PostMapping(value = "/chat/case")
    PostFastApiAiChatDto creatFastApiChat(PostFastApiUserChatDto postFastApiUserChatDto);
}
