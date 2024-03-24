package Ness.Backend.domain.schedule.dto.response;

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
    private String info;

    @Schema(description = "스케쥴 위치", example = "이화여대 ECC")
    private String location;

    /*
    @JsonProperty("scheduleDate")
    @Schema(description = "스케쥴 날짜", example = "01월 31일 10시 30분")
    private ScheduleDateDto scheduleDateDto;
     */

    @Schema(description = "스케쥴 날짜 및 시간", example = "2024-03-08T15:07:27.056103+09:00")
    private ZonedDateTime date;

    @Builder
    public GetScheduleDto(Long id, String info, String location, ZonedDateTime date){
        this.id = id;
        this.info = info;
        this.location = location;
        this.date = date;
    }
}
