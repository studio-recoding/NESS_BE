package Ness.Backend.domain.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFastApiScheduleDto {
    @JsonProperty("schedule_id")
    private Long schedule_id;

    @JsonProperty("member_id")
    private Long member_id;
}
