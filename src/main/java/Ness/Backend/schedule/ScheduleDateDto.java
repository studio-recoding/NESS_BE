package Ness.Backend.schedule;

import Ness.Backend.domain.ScheduleDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDateDto {
    @Schema(description = "스케쥴 시간", example = "12:00 PM")
    private String time;

    @Schema(description = "스케쥴 날짜", example = "2022-02-15")
    private String date;
}
