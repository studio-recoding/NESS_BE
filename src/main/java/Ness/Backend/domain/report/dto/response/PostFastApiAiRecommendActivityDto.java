package Ness.Backend.domain.report.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiAiRecommendActivityDto {
    @JsonProperty("ness")
    private String answer;

    @JsonProperty("activityList")
    private List<PostFastApiAiActivityDto> activityList;
}
