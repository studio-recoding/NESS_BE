package Ness.Backend.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiUserChatDto {
    @JsonProperty("prompt")
    private String message;

    @JsonProperty("persona")
    private String persona;
}
