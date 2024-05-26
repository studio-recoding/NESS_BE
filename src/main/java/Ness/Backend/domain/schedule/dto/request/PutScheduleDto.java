package Ness.Backend.domain.schedule.dto.request;


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
public class PutScheduleDto {
    @Schema(description = "스케쥴의 DB 기본키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "스케쥴 텍스트 내용", example = "AI 공부")
    @JsonProperty("title")
    private String info;

    @Schema(description = "스케쥴 위치", example = "이화여대 ECC")
    private String location;

    @Schema(description = "스케쥴 사람", example = "영희")
    private String person;

    @Schema(description = "스케쥴 시작 시간", example = "2024-03-08T15:07:27.056103+09:00")
    @JsonProperty("start")
    private ZonedDateTime startTime;

    @Schema(description = "스케쥴 끝 시간", example = "2024-03-08T15:07:27.056103+09:00")
    @JsonProperty("end")
    private ZonedDateTime endTime;

    @Schema(description = "스케쥴 카테고리 DB 넘버", example = "0")
    private Long categoryNum;

    @JsonProperty("originalTime")
    private ZonedDateTime originalTime;
}