package Ness.Backend.domain.main.dto.response;

import Ness.Backend.domain.report.dto.response.PostFastApiAiActivityDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class GetMainDto {
    // 한줄 추천 데이터
    @Schema(description = "한줄 추천 내용", example = "방학에는 역시 개발 공부죠!")
    @JsonProperty("recommend")
    private String recommendText;

    // 한줄 추천 활동 리스트
    private List<PostFastApiAiActivityDto> activityList;

    @Builder
    public GetMainDto(String recommendText, List<PostFastApiAiActivityDto> activityList){
        // 한줄 추천 데이터
        this.recommendText = recommendText;
        this.activityList = activityList;
    }
}
