package Ness.Backend.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequestDto {
    @Schema(description = "맴버 고유 인식 넘버", example = "0")
    private Long member_id;
}
