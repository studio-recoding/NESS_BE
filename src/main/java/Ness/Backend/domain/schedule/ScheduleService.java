package Ness.Backend.domain.schedule;

import Ness.Backend.domain.category.CategoryRepository;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.chat.ChatRepository;
import Ness.Backend.domain.chat.ChatService;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.chat.entity.ChatType;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.dto.request.*;
import Ness.Backend.domain.schedule.dto.response.GetScheduleListDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDetailDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDto;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.global.fastApi.FastApiDeleteScheduleApi;
import Ness.Backend.global.fastApi.FastApiPostScheduleApi;
import Ness.Backend.global.fastApi.FastApiPutScheduleApi;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final FastApiPostScheduleApi fastApiPostScheduleApi;
    private final FastApiDeleteScheduleApi fastApiDeleteScheduleApi;
    private final FastApiPutScheduleApi fastApiPutScheduleApi;

    // 한 달치 스케쥴 가져오는 로직
    @Transactional(readOnly = true)
    public GetScheduleListDto getOneMonthUserSchedule(Long memberId, String date){
        log.info("getOneMonthUserSchedule called by "+ memberId);
        // 년도, 월, 일 추출
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        return makeScheduleListDto(
                scheduleRepository
                        .findOneMonthSchedulesByMember_Id(memberId, year, month));
    }

    // 하루치 스케쥴 가져오는 로직(Create, Delete에서 사용)
    @Transactional(readOnly = true)
    public GetScheduleListDto getOneDayUserSchedule(Long memberId, ZonedDateTime date){
        return makeScheduleListDto(
                scheduleRepository
                        .findOneDaySchedulesByMember_Id(memberId, date));
    }

    /* 사용자가 직접 변경한 스케쥴 RDB에 저장하는 로직 */
    public GetScheduleListDto changeSchedule(Long memberId, PutScheduleDto putScheduleDto){
        Schedule schedule = scheduleRepository.findScheduleById(putScheduleDto.getId());
        Category category = categoryRepository.findCategoryById(putScheduleDto.getCategoryNum());
        
        //RDB에서 변경
        schedule.changeSchedule(
                putScheduleDto.getInfo(),
                putScheduleDto.getLocation(),
                putScheduleDto.getPerson(),
                putScheduleDto.getStartTime(),
                putScheduleDto.getEndTime(),
                category);

        //VectorDB에서 변경
        ZonedDateTime endTime = putScheduleDto.getEndTime();
        if(endTime == null){
            endTime =  putScheduleDto.getStartTime();
        }

        PutFastApiScheduleDto dto = PutFastApiScheduleDto.builder()
                .info(putScheduleDto.getInfo())
                .location(putScheduleDto.getLocation())
                .person(putScheduleDto.getPerson())
                .startTime(putScheduleDto.getStartTime())
                .endTime(endTime)
                .category(category.getName())
                .category_id(category.getId())
                .member_id(memberId)
                .schedule_id(putScheduleDto.getId())
                .build();

        ResponseEntity<JsonNode> responseNode = fastApiPutScheduleApi.putFastApiSchedule(dto);

        if (responseNode.getStatusCode() == HttpStatusCode.valueOf(201)) {
            log.info("Succeed to put data in Vector DB");
        } else {
            log.error("Failed to put data in Vector DB");
        }

        return getOneDayUserSchedule(memberId, putScheduleDto.getOriginalTime().withZoneSameInstant(ZoneId.of("Asia/Seoul")));
    }

    /* 사용자가 직접 삭제한 스케쥴 */
    public GetScheduleListDto deleteSchedule(Long memberId, Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        ZonedDateTime scheduleTime = schedule.getStartTime().withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        //VectorDB에서 삭제
        DeleteFastApiScheduleDto dto = DeleteFastApiScheduleDto.builder()
                .member_id(memberId)
                .schedule_id(scheduleId)
                .build();

        ResponseEntity<JsonNode> responseNode = fastApiDeleteScheduleApi.deleteFastApiSchedule(dto);
        if (responseNode.getStatusCode() == HttpStatusCode.valueOf(204)) {
            log.info("Succeed to delete data in Vector DB");
        } else {
            log.error("Failed to delete data in Vector DB");
        }

        //RDB에서 삭제
        scheduleRepository.delete(schedule);

        return getOneDayUserSchedule(memberId, scheduleTime);
    }

    /* 사용자가 AI가 생성한 스케쥴을 Accept/Deny한 여부에 따라서 채팅 및 스케쥴 저장 */
    public GetChatListDto postAiScheduleAccept(Long memberId, Boolean idAccepted, Long chatId, PostScheduleDto postScheduleDto){
        Member member = memberRepository.findMemberById(memberId);
        Category category = categoryRepository.findCategoryById(postScheduleDto.getCategoryNum());

        if(idAccepted){
            /* 사용자가 Accept 했으면 스케쥴 생성하기 */
            Chat chat = chatRepository.findChatById(chatId);

            Schedule newSchedule = Schedule.builder()
                    .info(postScheduleDto.getInfo())
                    .location(postScheduleDto.getLocation())
                    .person(postScheduleDto.getPerson())
                    .startTime(postScheduleDto.getStartTime())
                    .endTime(postScheduleDto.getEndTime())
                    .member(member)
                    .category(category)
                    .chat(chat)
                    .build();

            scheduleRepository.save(newSchedule);

            chatService.createNewChat("일정을 추가해드렸습니다:)", ChatType.AI, 1, member);

        } else {
            chatService.createNewChat("일정 추가를 취소했습니다.\n더 필요한 것이 있으시면 알려주세요!", ChatType.AI, 1, member);
        }

        // 모든 채팅 내역 반환
        return chatService.getOneWeekUserChat(memberId);
    }


    /* 사용자가 직접 생성한 스케쥴을 RDB & VectorDB에 저장 */
    public GetScheduleListDto postNewUserSchedule(Long memberId, PostScheduleDto postScheduleDto){
        Member memberEntity = memberRepository.findMemberById(memberId);
        Category category = categoryRepository.findCategoryById(postScheduleDto.getCategoryNum());

        //새로운 채팅 생성
        Schedule newSchedule = Schedule.builder()
                .info(postScheduleDto.getInfo())
                .location(postScheduleDto.getLocation())
                .person(postScheduleDto.getPerson())
                .startTime(postScheduleDto.getStartTime())
                .endTime(postScheduleDto.getEndTime())
                .member(memberEntity)
                .category(category)
                //.chat() //사용자가 직접 생성했으므로 연관관계 없음
                .build();

        scheduleRepository.save(newSchedule);

        postNewAiSchedule(
                postScheduleDto.getInfo(),
                postScheduleDto.getLocation(),
                postScheduleDto.getPerson(),
                postScheduleDto.getStartTime(),
                postScheduleDto.getEndTime(),
                category.getName(),
                category.getId(),
                newSchedule.getMember().getId(),
                newSchedule.getId());

        log.info("Succeed to save user created schedule data in RDB & VectorDB");
        return getOneDayUserSchedule(memberId, newSchedule.getStartTime().withZoneSameInstant(ZoneId.of("Asia/Seoul")));
    }

    /* 새로운 스케쥴을 VectorDB에 저장하는 API 호출 */
    public void postNewAiSchedule(String info, String location, String person,
                                  ZonedDateTime startTime, ZonedDateTime endTime,
                                  String category, Long category_id, Long memberId, Long scheduleId){

        if(endTime == null){
            endTime = startTime;
        }

        PostFastApiScheduleDto dto = PostFastApiScheduleDto.builder()
                .info(info)
                .location(location)
                .person(person)
                .startTime(startTime)
                .endTime(endTime)
                .category(category)
                .category_id(category_id)
                .member_id(memberId)
                .schedule_id(scheduleId)
                .build();

        ResponseEntity<JsonNode> responseNode = fastApiPostScheduleApi.creatFastApiSchedule(dto);
        if (responseNode.getStatusCode() == HttpStatusCode.valueOf(201)) {
            log.info("Succeed to save data in Vector DB");
        } else {
            log.error("Failed to save data in Vector DB");
        }
    }

    /* 리스트 DTO 만들어서 반환하는 로직 */
    public GetScheduleListDto makeScheduleListDto(List<Schedule> scheduleList){
        // ScheduleListResponseDto에 매핑
        List<GetScheduleDto> getScheduleDtos = scheduleList.stream()
                .map(schedule -> GetScheduleDto.builder()
                        .id(schedule.getId())
                        .category(schedule.getCategory().getName())
                        .categoryNum(schedule.getCategory().getId())
                        .categoryColor(schedule.getCategory().getColor())
                        .info(schedule.getInfo())
                        .startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime())
                        .details(GetScheduleDetailDto.builder()
                                .person(schedule.getPerson())
                                .location(schedule.getLocation())
                                .build())
                        .build())
                .toList();
        return new GetScheduleListDto(getScheduleDtos);
    }
}
