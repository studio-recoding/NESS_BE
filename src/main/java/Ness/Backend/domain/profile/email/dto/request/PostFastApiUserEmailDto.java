package Ness.Backend.domain.profile.email.dto.request;

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
public class PostFastApiUserEmailDto {
    @JsonProperty("member_id")
    private int member_id;

    @JsonProperty("user_persona")
    private String user_persona;

    @JsonProperty("schedule_datetime_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime schedule_datetime_start;

    @JsonProperty("schedule_datetime_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime schedule_datetime_end;
}
