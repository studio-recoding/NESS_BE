package Ness.Backend.schedule;

import Ness.Backend.entity.Member;
import Ness.Backend.entity.Schedule;
import Ness.Backend.entity.ScheduleDate;
import Ness.Backend.member.MemberRepository;
import Ness.Backend.schedule.dto.ScheduleCreateRequestDto;
import Ness.Backend.schedule.dto.ScheduleDateDto;
import Ness.Backend.schedule.dto.ScheduleDto;
import Ness.Backend.schedule.dto.ScheduleListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    public ScheduleListResponseDto findOneUserSchedule(Long id){
        List<Schedule> scheduleList = scheduleRepository.findByMember_Id(id);

        // ScheduleListResponseDto에 매핑
        List<ScheduleDto> scheduleDtos = scheduleList.stream()
                .map(schedule -> new ScheduleDto.ScheduleDtoBuilder()
                        .id(schedule.getId())
                        .info(schedule.getInfo())
                        .location(schedule.getLocation())
                        .scheduleDateDto(new ScheduleDateDto.ScheduleDateDtoBuilder()
                                .time(schedule.getScheduleDate().getTime())
                                .date(schedule.getScheduleDate().getDate())
                                .build())
                        .build())
                .toList();
        return new ScheduleListResponseDto(scheduleDtos);
    }

    @Transactional
    public Long createNewSchedule(ScheduleCreateRequestDto scheduleCreateRequestDto){
        Member memberEntity = memberRepository.findMemberById(scheduleCreateRequestDto.getMember_id());

        ScheduleDate newScheduleDate = ScheduleDate.builder()
                .time(scheduleCreateRequestDto.getScheduleDateDto().getTime())
                .date(scheduleCreateRequestDto.getScheduleDateDto().getDate())
                .build();

        //새로운 채팅 생성
        Schedule newSchedule = Schedule.builder()
                .info(scheduleCreateRequestDto.getInfo())
                .location(scheduleCreateRequestDto.getLocation())
                .person(scheduleCreateRequestDto.getPerson())
                .scheduleDate(newScheduleDate)
                .member(memberEntity)
                //.category() //이 연관관계들은 나중에 넣어야 함
                //.chat()
                .build();

        scheduleRepository.save(newSchedule);
        return newSchedule.getId(); // 저장한 Chat 확인용
    }
}
