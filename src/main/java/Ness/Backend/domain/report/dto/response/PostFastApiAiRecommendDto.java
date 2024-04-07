package Ness.Backend.domain.report.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiAiRecommendDto {
    @JsonProperty("ness")
    private String answer;
}
