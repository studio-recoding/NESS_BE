package Ness.Backend.domain.schedule;

import Ness.Backend.domain.schedule.dto.ScheduleCreateFastApiDto;
import Ness.Backend.domain.schedule.dto.ScheduleDto;
import Ness.Backend.domain.schedule.dto.ScheduleListResponseDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.schedule.dto.ScheduleCreateRequestDto;
import Ness.Backend.global.fastApi.FastApiScheduleApi;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final FastApiScheduleApi fastApiScheduleApi;

    public ScheduleListResponseDto findOneUserSchedule(Long id){
        List<Schedule> scheduleList = scheduleRepository.findByMember_Id(id);

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

        ScheduleCreateFastApiDto dto = ScheduleCreateFastApiDto.builder()
                .info(scheduleCreateRequestDto.getInfo())
                .location(scheduleCreateRequestDto.getLocation())
                .person(scheduleCreateRequestDto.getPerson())
                .date(scheduleCreateRequestDto.getDate())
                .category("카테고리 없음") //일단은 null 처리하기
                .member_id(newSchedule.getMember().getId())
                .schedule_id(newSchedule.getId())
                .build();

        ResponseEntity<JsonNode> responseNode = fastApiScheduleApi.creatFastApiSchedule(dto);
        if (responseNode.getStatusCode() == HttpStatusCode.valueOf(200)) {
            log.info("Succeed to save data in Vector DB");
        } else {
            log.error("Failed to save data in Vector DB");
        }

        return newSchedule.getId(); // 저장한 Chat 확인용
    }
}
