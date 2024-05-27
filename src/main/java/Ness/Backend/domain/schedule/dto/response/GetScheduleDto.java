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
    @Schema(description = "스케쥴 고유 인식 넘버", example = "0")
    private Long id;

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

    @Schema(description = "스케줄 카테고리 색상", example = "#FFFFFF")
    @JsonProperty("categoryColor")
    private String categoryColor;

    @Schema(description = "스케줄 디테일", example = "위치, 사람, id 등")
    @JsonProperty("details")
    private GetScheduleDetailDto details;


    @Schema(description = "스케줄에 대한 네스의 한줄 코멘트", example = "공대에서 공부하시는군요. 효율적인 학습을 위해 좋은 환경을 만드세요!")
    @JsonProperty("nessComment")
    private String nessComment;

    @Builder
    public GetScheduleDto(Long id, String category, Long categoryNum, String categoryColor,
                          String info, ZonedDateTime startTime, ZonedDateTime endTime,
                          GetScheduleDetailDto details, String nessComment){
        this.id = id;
        this.category = category;
        this.categoryNum = categoryNum;
        this.categoryColor = categoryColor;
        this.info = info;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
        this.nessComment = nessComment;
    }
}
