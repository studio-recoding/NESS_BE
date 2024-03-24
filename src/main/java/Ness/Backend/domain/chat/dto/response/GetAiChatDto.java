package Ness.Backend.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAiChatDto {
    @JsonProperty("ai")
    private String answer;
}
