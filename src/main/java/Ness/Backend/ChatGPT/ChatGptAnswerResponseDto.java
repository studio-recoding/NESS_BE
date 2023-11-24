package Ness.Backend.ChatGPT;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatGptAnswerResponseDto {
    private String answer;

    public ChatGptAnswerResponseDto(String answer){
        this.answer = answer;
    }
}