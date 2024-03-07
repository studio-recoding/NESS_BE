package Ness.Backend.domain.schedule;

import Ness.Backend.domain.schedule.dto.ScheduleCreateRequestDto;
import Ness.Backend.domain.schedule.dto.ScheduleListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Schedule API", description = "사용자의 스케쥴 내역 관련 API")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping("/schedule/user")
    @Operation(summary = "특정 사용자의 스케쥴 내역", description = "특정 사용자의 모든 스케쥴 내역을 반환하는 API 입니다.")
    public ResponseEntity<ScheduleListResponseDto> getOneUserSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto){
        ScheduleListResponseDto oneUserSchedules = scheduleService.findOneUserSchedule(scheduleRequestDto.getMember_id());
        return new ResponseEntity<>(oneUserSchedules, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/schedule/new")
    @Operation(summary = "새로운 스케쥴 내역", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> createSchedule(@RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto){
        Long userId  = scheduleService.createNewSchedule(scheduleCreateRequestDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(200));
    }

}
