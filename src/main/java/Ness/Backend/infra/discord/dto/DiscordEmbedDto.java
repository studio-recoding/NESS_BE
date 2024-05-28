package Ness.Backend.infra.discord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiscordEmbedDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;
}