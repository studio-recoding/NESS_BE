package Ness.Backend.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class GetScheduleDto {
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
    public GetScheduleDto(String category, Long categoryNum, String info, ZonedDateTime startTime, ZonedDateTime endTime, GetScheduleDetailDto details){
        this.category = category;
        this.categoryNum = categoryNum;
        this.info = info;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
    }
}
