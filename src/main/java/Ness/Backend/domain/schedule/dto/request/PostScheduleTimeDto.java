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
public class PostScheduleTimeDto {
    @Schema(description = "스케쥴의 DB 기본키", example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "스케쥴 시작 시간", example = "2024-03-08T15:07:27.056103+09:00")
    @JsonProperty("start")
    private ZonedDateTime startTime;

    @Schema(description = "스케쥴 끝 시간", example = "2024-03-08T15:07:27.056103+09:00")
    @JsonProperty("end")
    private ZonedDateTime endTime;
}
