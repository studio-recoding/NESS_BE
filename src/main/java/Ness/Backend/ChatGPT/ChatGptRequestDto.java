package Ness.Backend.ChatGPT;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ChatGptRequestDto {
    private String model;

    @JsonProperty("messages")
    private List<MessageRequestDto> messages;

    private Double temperature;

    @Builder
    public ChatGptRequestDto(String model,
                             List<MessageRequestDto> messages,
                             Double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;

    }
}
