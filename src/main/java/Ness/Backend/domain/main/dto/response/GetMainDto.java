package Ness.Backend.domain.main.dto.response;

import Ness.Backend.domain.schedule.dto.response.GetScheduleDetailDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMainDto {
    // 한줄 추천 데이터
    @Schema(description = "한줄 추천 고유 인식 넘버", example = "0")
    private Long recommendId;

    @Schema(description = "한줄 추천 내용", example = "방학에는 역시 개발 공부죠!")
    @JsonProperty("recommend")
    private String recommendText;

    // 스케쥴 데이터(1개)
    @Schema(description = "스케쥴 고유 인식 넘버", example = "0")
    private Long scheduleId;

    @Schema(description = "스케쥴 텍스트 내용", example = "AI 공부")
    @JsonProperty("title")
    private String info;

    @Schema(description = "스케쥴 시작 날짜 및 시간", example = "2024-03-08T15:07:27.056103+09:00")
    @JsonProperty("start")
    private ZonedDateTime startTime;

    @Schema(description = "스케쥴 끝 날짜 및 시간", example = "2024-03-08T15:07:27.056103+09:00")
    @JsonProperty("end")
    private ZonedDateTime endTime;

    @Schema(description = "스케줄 카테고리", example = "공부")
    @JsonProperty("category")
    private String category;

    @Schema(description = "스케줄 카테고리 숫자", example = "1")
    @JsonProperty("categoryNum")
    private Long categoryNum;

    @Schema(description = "스케줄 디테일", example = "위치, 사람, id 등")
    @JsonProperty("details")
    private GetScheduleDetailDto details;

    @Builder
    public GetMainDto(Long recommendId, String recommendText,
                      Long scheduleId, String category, Long categoryNum, String info,
                      ZonedDateTime startTime, ZonedDateTime endTime, GetScheduleDetailDto details){
        // 한줄 추천 데이터
        this.recommendId = recommendId;
        this.recommendText = recommendText;

        // 스케쥴 데이터(1개)
        this.scheduleId = scheduleId;
        this.category = category;
        this.categoryNum = categoryNum;
        this.info = info;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
    }
}
