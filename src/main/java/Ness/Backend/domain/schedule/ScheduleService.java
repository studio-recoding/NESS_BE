package Ness.Backend.domain.schedule;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.dto.request.PostFastApiScheduleDto;
import Ness.Backend.domain.schedule.dto.request.PostScheduleDto;
import Ness.Backend.domain.schedule.dto.request.PostScheduleTimeDto;
import Ness.Backend.domain.schedule.dto.response.GetOneMonthSchedulesDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDetailDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDto;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.global.fastApi.FastApiScheduleApi;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final FastApiScheduleApi fastApiScheduleApi;

    @Transactional(readOnly = true)
    public GetOneMonthSchedulesDto getOneMonthUserSchedule(Long id, String date){
        log.info("getOneMonthUserSchedule called by "+ id);
        // 년도, 월, 일 추출
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        List<Schedule> scheduleList = scheduleRepository.findOneMonthSchedulesByMember_Id(id, year, month);
        
        // ScheduleListResponseDto에 매핑
        List<GetScheduleDto> getScheduleDtos = scheduleList.stream()
                .map(schedule -> GetScheduleDto.builder()
                        .id(schedule.getId())
                        .category(schedule.getCategory().getName())
                        .categoryNum(schedule.getCategory().getId())
                        .info(schedule.getInfo())
                        .startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime())
                        .details(GetScheduleDetailDto.builder()
                                .person(schedule.getPerson())
                                .location(schedule.getLocation())
                                .build())
                        .build())
                .toList();
        return new GetOneMonthSchedulesDto(getScheduleDtos);
    }

    public void changeScheduleTime(Long id, PostScheduleTimeDto postScheduleTimeDto){
        Schedule schedule = scheduleRepository.findScheduleById(postScheduleTimeDto.getId());
        schedule.changeTime(postScheduleTimeDto.getStartTime(), postScheduleTimeDto.getEndTime());
    }

    public Long postNewUserSchedule(Long id, PostScheduleDto postScheduleDto){
        log.info("postNewUserSchedule called by "+ id);
        Member memberEntity = memberRepository.findMemberById(id);

        //새로운 채팅 생성
        Schedule newSchedule = Schedule.builder()
                .info(postScheduleDto.getInfo())
                .location(postScheduleDto.getLocation())
                .person(postScheduleDto.getPerson())
                .startTime(postScheduleDto.getStartTime())
                .endTime(postScheduleDto.getEndTime())
                .member(memberEntity)
                //.category() //이 연관관계들은 나중에 넣어야 함
                //.chat()
                .build();

        scheduleRepository.save(newSchedule);

        postNewAiSchedule(
                postScheduleDto.getInfo(),
                postScheduleDto.getLocation(),
                postScheduleDto.getPerson(),
                postScheduleDto.getStartTime(),
                postScheduleDto.getEndTime(),
                "카테고리 없음",
                newSchedule.getMember().getId(),
                newSchedule.getId());

        return newSchedule.getId(); // 저장한 Chat 확인용
    }

    public void postNewAiSchedule(String info, String location, String person,
                                  ZonedDateTime startTime, ZonedDateTime endTime,
                                  String category, Long memberId, Long scheduleId){

        PostFastApiScheduleDto dto = PostFastApiScheduleDto.builder()
                .info(info)
                .location(location)
                .person(person)
                .startTime(startTime)
                .endTime(endTime)
                .category(category) //일단은 null 처리하기
                .member_id(memberId)
                .schedule_id(scheduleId)
                .build();

        ResponseEntity<JsonNode> responseNode = fastApiScheduleApi.creatFastApiSchedule(dto);
        if (responseNode.getStatusCode() == HttpStatusCode.valueOf(201)) {
            log.info("Succeed to save data in Vector DB");
        } else {
            log.error("Failed to save data in Vector DB");
        }
    }
}
