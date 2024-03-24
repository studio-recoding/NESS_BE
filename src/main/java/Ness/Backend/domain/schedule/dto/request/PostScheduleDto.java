package Ness.Backend.domain.schedule.dto.request;

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
public class PostScheduleDto {
    @Schema(description = "스케쥴 텍스트 내용", example = "AI 공부")
    private String info;

    @Schema(description = "스케쥴 위치", example = "이화여대 ECC")
    private String location;

    @Schema(description = "스케쥴 사람", example = "영희")
    private String person;

    @Schema(description = "스케쥴 시간", example = "2024-03-08T15:07:27.056103+09:00")
    private ZonedDateTime date;
}