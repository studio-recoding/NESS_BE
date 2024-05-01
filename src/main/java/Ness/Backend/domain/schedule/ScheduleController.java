package Ness.Backend.domain.schedule;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.dto.request.PostScheduleDto;
import Ness.Backend.domain.schedule.dto.request.PutScheduleDto;
import Ness.Backend.domain.schedule.dto.response.GetOneMonthSchedulesDto;
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

    @GetMapping("")
    @Operation(summary = "특정 사용자의 한달치 스케쥴 내역", description = "&month=2024-01 와 같은 형식으로 데이터가 전달됩니다.")
    public ResponseEntity<GetOneMonthSchedulesDto> getUserSchedule(@AuthUser Member member, @RequestParam String month){
        GetOneMonthSchedulesDto oneUserMonthSchedules = scheduleService.getOneMonthUserSchedule(member.getId(), month);
        return new ResponseEntity<>(oneUserMonthSchedules, HttpStatusCode.valueOf(200));
    }

    @PostMapping("")
    @Operation(summary = "새로운 스케쥴 생성", description = "새로운 스케쥴 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> postUserSchedule(@AuthUser Member member, @RequestBody PostScheduleDto postScheduleDto){
        Long userId  = scheduleService.postNewUserSchedule(member.getId(), postScheduleDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(201));
    }

    @PutMapping("")
    @Operation(summary = "하나의 스케쥴 변경", description = "하나의 스케쥴의 정보를 변경하는 API로 스케쥴, 위치, 사람, 시간 모두 PUT으로 처리합니다.")
    public ResponseEntity<Long> putUserSchedule(@AuthUser Member member, @RequestBody PutScheduleDto putScheduleDto){
        Long userId = scheduleService.changeSchedule(member.getId(), putScheduleDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(200));
    }
}