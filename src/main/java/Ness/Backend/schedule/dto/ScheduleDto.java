package Ness.Backend.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    @Schema(description = "스케쥴 고유 인식 넘버", example = "0")
    private Long id;

    @Schema(description = "스케쥴 텍스트 내용", example = "AI 공부")
    private String info;

    @Schema(description = "스케쥴 위치", example = "이화여대 ECC")
    private String location;

    @JsonProperty("scheduleDate")
    @Schema(description = "스케쥴 날짜", example = "01월 31일 10시 30분")
    private ScheduleDateDto scheduleDateDto;
}
