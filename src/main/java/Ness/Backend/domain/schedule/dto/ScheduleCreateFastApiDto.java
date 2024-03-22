package Ness.Backend.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleCreateFastApiDto {

    private Long schedule_id;

    private Long member_id;

    private String info;

    private String location;

    private String person;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime date;

    private String category;
}
