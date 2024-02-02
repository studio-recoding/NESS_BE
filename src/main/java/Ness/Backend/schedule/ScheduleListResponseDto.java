package Ness.Backend.schedule;

import Ness.Backend.chat.ChatDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleListResponseDto {
    private List<ScheduleDto> scheduleList;
}
