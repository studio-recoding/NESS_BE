package Ness.Backend.ChatGPT;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
    private String role;
    private String content;

    @Builder
    public MessageResponseDto(String role, String content){
        this.role = role;
        this.content = content;
    }
}