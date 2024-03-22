package Ness.Backend.domain.schedule;

import Ness.Backend.domain.report.dto.ReportMemoryDto;
import Ness.Backend.domain.report.dto.ReportMemoryListResponseDto;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.schedule.dto.ScheduleDto;
import Ness.Backend.domain.schedule.dto.ScheduleListResponseDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.schedule.dto.ScheduleCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    public ScheduleListResponseDto findOneUserMonthSchedule(Long id, String date){
        // 년도, 월, 일 추출
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        List<Schedule> scheduleList = scheduleRepository.findSchedulesByMember_IdAndMonthOrderByDateAsc(id, year, month);

        // ScheduleListResponseDto에 매핑
        List<ScheduleDto> scheduleDtos = scheduleList.stream()
                .map(schedule -> ScheduleDto.builder()
                        .id(schedule.getId())
                        .info(schedule.getInfo())
                        .date(schedule.getDate())
                        .location(schedule.getLocation())
                        .build())
                .toList();
        return new ScheduleListResponseDto(scheduleDtos);
    }

    public ScheduleListResponseDto findOneUserDaySchedule(Long id, String date){
        // 년도, 월, 일 추출
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        List<Schedule> scheduleList = scheduleRepository.findSchedulesByMember_IdAndDayOrderByDateAsc(id, year, month, day);

        // ScheduleListResponseDto에 매핑
        List<ScheduleDto> scheduleDtos = scheduleList.stream()
                .map(schedule -> ScheduleDto.builder()
                        .id(schedule.getId())
                        .info(schedule.getInfo())
                        .date(schedule.getDate())
                        .location(schedule.getLocation())
                        .build())
                .toList();
        return new ScheduleListResponseDto(scheduleDtos);
    }

    @Transactional
    public Long createNewSchedule(Long id, ScheduleCreateRequestDto scheduleCreateRequestDto){
        Member memberEntity = memberRepository.findMemberById(id);

        /*
        ScheduleDate newScheduleDate = ScheduleDate.builder()
                .time(scheduleCreateRequestDto.getScheduleDateDto().getTime())
                .date(scheduleCreateRequestDto.getScheduleDateDto().getDate())
                .build();

         */

        //새로운 채팅 생성
        Schedule newSchedule = Schedule.builder()
                .info(scheduleCreateRequestDto.getInfo())
                .location(scheduleCreateRequestDto.getLocation())
                .person(scheduleCreateRequestDto.getPerson())
                .date(scheduleCreateRequestDto.getDate())
                //.date(LocalDateTime.now(ZoneId.of("Asia/Seoul")).atZone(ZoneId.of("Asia/Seoul")))
                .member(memberEntity)
                //.category() //이 연관관계들은 나중에 넣어야 함
                //.chat()
                .build();

        scheduleRepository.save(newSchedule);
        return newSchedule.getId(); // 저장한 Chat 확인용
    }
}
