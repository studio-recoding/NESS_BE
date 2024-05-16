package Ness.Backend.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* ChatGPT에게서 받아오는 답변 */
public class PostFastApiAiChatDto {
    @JsonProperty("ness")
    private String answer;

    @JsonProperty("case")
    private int caseNumber;
}
