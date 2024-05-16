package Ness.Backend.domain.chat.dto.request;

import Ness.Backend.domain.chat.entity.ChatType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* AI에게 보내는 유저 채팅 */
public class PostFastApiUserChatDto {
    @JsonProperty("prompt")
    private String message;

    @JsonProperty("persona")
    private String persona;

    @JsonProperty("chatType")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ChatType chatType;
}
