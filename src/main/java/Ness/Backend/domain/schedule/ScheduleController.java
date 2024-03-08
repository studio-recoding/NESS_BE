package Ness.Backend.domain.schedule;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.dto.ScheduleCreateRequestDto;
import Ness.Backend.domain.schedule.dto.ScheduleListResponseDto;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Schedule API", description = "사용자의 스케쥴 내역 관련 API")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping("/schedule")
    @Operation(summary = "특정 사용자의 스케쥴 내역", description = "프로덕트용 API 입니다. 특정 사용자의 모든 스케쥴 내역을 반환합니다.")
    public ResponseEntity<ScheduleListResponseDto> getOneUserSchedule(@AuthUser Member member){
        ScheduleListResponseDto oneUserSchedules = scheduleService.findOneUserSchedule(member.getId());
        return new ResponseEntity<>(oneUserSchedules, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/schedule/new")
    @Operation(summary = "새로운 스케쥴 내역", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> createSchedule(@AuthUser Member member, @RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto){
        Long userId  = scheduleService.createNewSchedule(member.getId(), scheduleCreateRequestDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(200));
    }

}
