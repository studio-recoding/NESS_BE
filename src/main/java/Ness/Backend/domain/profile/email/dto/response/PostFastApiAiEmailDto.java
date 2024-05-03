package Ness.Backend.domain.profile.email.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiAiEmailDto {
    @JsonProperty("text")
    private String text;

    // DALL-E에서 생성한 이미지 링크
    @JsonProperty("image")
    private String image;
}
