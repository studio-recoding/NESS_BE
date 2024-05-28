package Ness.Backend.infra.discord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiscordMessageDto {
    @JsonProperty("content")
    private String content;

    @JsonProperty("embeds")
    private List<DiscordEmbedDto> embeds;
}

/*
* {
  "content": "string"
  "embeds": [
  	{
  		"title": "string"
  		"description": "string"
	},
	...
  ]
}
*
* */