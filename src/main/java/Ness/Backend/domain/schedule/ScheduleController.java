package Ness.Backend.domain.schedule;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.dto.request.PostScheduleDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleListDto;
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
@RequestMapping("schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping("/month")
    @Operation(summary = "특정 사용자의 한달 스케쥴 내역", description = "&month=2024-01 와 같은 형식으로 데이터가 전달됩니다.")
    public ResponseEntity<GetScheduleListDto> getOneMonthUserSchedule(@AuthUser Member member, @RequestParam String month){
        GetScheduleListDto oneUserMonthSchedules = scheduleService.findOneUserMonthSchedule(member.getId(), month);
        return new ResponseEntity<>(oneUserMonthSchedules, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/day")
    @Operation(summary = "특정 사용자의 하루 스케쥴 내역", description = "&day=2024-01-01와 같은 형식으로 데이터가 전달됩니다.")
    public ResponseEntity<GetScheduleListDto> getOneUserDaySchedule(@AuthUser Member member, @RequestParam String day){
        GetScheduleListDto oneUserDaySchedules = scheduleService.findOneUserDaySchedule(member.getId(), day);
        return new ResponseEntity<>(oneUserDaySchedules, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/new")
    @Operation(summary = "새로운 스케쥴 내역", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> createSchedule(@AuthUser Member member, @RequestBody PostScheduleDto postScheduleDto){
        Long userId  = scheduleService.createNewSchedule(member.getId(), postScheduleDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/dev/new")
    @Operation(summary = "새로운 스케쥴 내역", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> createDevSchedule(@RequestBody PostScheduleDto postScheduleDto){
        Long userId  = scheduleService.createNewSchedule(1L, postScheduleDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(200));
    }
}