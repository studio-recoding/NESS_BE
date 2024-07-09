package Ness.Backend.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFastApiScheduleDto {

    @JsonProperty("schedule_id")
    private Long schedule_id;

    @JsonProperty("member_id")
    private Long member_id;

    @JsonProperty("data")
    private String info;

    @JsonProperty("location")
    private String location;

    @JsonProperty("person")
    private String person;

    @JsonProperty("schedule_datetime_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime startTime;

    @JsonProperty("schedule_datetime_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime endTime;

    @JsonProperty("category")
    private String category;

    @JsonProperty("category_id")
    private Long category_id;

    @JsonProperty("category_color")
    private String category_color;
}
