package Ness.Backend.ChatGPT;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ChoiceDto implements Serializable {

    private Integer index;

    private MessageResponseDto message;

    @JsonProperty("finish_reason")
    private String finishReason;


    @Builder
    public ChoiceDto(Integer index, MessageResponseDto message, String finishReason) {
        //this.text = text;
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }
}