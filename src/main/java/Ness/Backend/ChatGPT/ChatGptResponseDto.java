package Ness.Backend.ChatGPT;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatGptResponseDto {
    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private List<ChoiceDto> choiceDtos;

    @Builder
    public ChatGptResponseDto(String id, String object,
                              LocalDate created, String model,
                              List<ChoiceDto> choiceDtos) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choiceDtos = choiceDtos;
    }
}
