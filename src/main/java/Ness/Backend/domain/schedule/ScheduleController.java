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
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/month/dev")
    @Operation(summary = "개발 테스트용 특정 사용자의 한달치 스케쥴 내역", description = "&month=2024-01 와 같은 형식으로 데이터가 전달됩니다.")
    public ResponseEntity<GetScheduleListDto> getUserSchedule(@RequestParam String month){
        GetScheduleListDto oneUserMonthSchedules = scheduleService.getOneMonthUserSchedule(1L, month);
        return new ResponseEntity<>(oneUserMonthSchedules, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/new/dev")
    @Operation(summary = "개발 테스트용 새로운 스케쥴 내역", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> postUserSchedule(@RequestBody PostScheduleDto postScheduleDto){
        Long userId  = scheduleService.postNewUserSchedule(1L, postScheduleDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/month")
    @Operation(summary = "특정 사용자의 한달치 스케쥴 내역", description = "&month=2024-01 와 같은 형식으로 데이터가 전달됩니다.")
    public ResponseEntity<GetScheduleListDto> getUserSchedule(@AuthUser Member member, @RequestParam String month){
        GetScheduleListDto oneUserMonthSchedules = scheduleService.getOneMonthUserSchedule(member.getId(), month);
        return new ResponseEntity<>(oneUserMonthSchedules, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/new")
    @Operation(summary = "새로운 스케쥴 내역", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> postUserSchedule(@AuthUser Member member, @RequestBody PostScheduleDto postScheduleDto){
        Long userId  = scheduleService.postNewUserSchedule(member.getId(), postScheduleDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(201));
    }
}