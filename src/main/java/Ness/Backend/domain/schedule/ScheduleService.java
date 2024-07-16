package Ness.Backend.domain.schedule;

import Ness.Backend.domain.bookmark.BookmarkRepository;
import Ness.Backend.domain.bookmark.entity.Bookmark;
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
import Ness.Backend.domain.schedule.dto.response.GetScheduleDetailDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleListDto;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.global.error.exception.NotFoundCategoryException;
import Ness.Backend.global.error.exception.NotFoundScheduleException;
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
    private final BookmarkRepository bookmarkRepository;
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
                category.getColor(),
                newSchedule.getMember().getId(),
                newSchedule.getId());

        log.info("Succeed to save user created schedule data in RDB & VectorDB");
        return getOneDayUserSchedule(memberId, newSchedule.getStartTime().withZoneSameInstant(ZoneId.of("Asia/Seoul")));
    }

    /* 사용자가 AI가 생성한 스케쥴을 Accept/Deny한 여부에 따라서 채팅 및 스케쥴 저장 */
    public GetChatListDto postAiScheduleAccept(Long memberId, Boolean idAccepted, Long chatId, PostMetaDataScheduleDto postMetaDataScheduleDto){
        Member member = memberRepository.findMemberById(memberId);
        Category category = categoryRepository.findCategoryById(postMetaDataScheduleDto.getCategoryNum());

        /* 사용자가 Accept 했으면 스케쥴 생성하기 */
        if(idAccepted){
            /* 카테고리 연견관계가 정상적인 경우*/
            if(category != null){
                Chat chat = chatRepository.findChatById(chatId);
                
                //RDB에 저장
                Schedule newSchedule = Schedule.builder()
                        .info(postMetaDataScheduleDto.getInfo())
                        .location(postMetaDataScheduleDto.getLocation())
                        .person(postMetaDataScheduleDto.getPerson())
                        .startTime(postMetaDataScheduleDto.getStartTime())
                        .endTime(postMetaDataScheduleDto.getEndTime())
                        .member(member)
                        .category(category)
                        .chat(chat)
                        .build();

                scheduleRepository.save(newSchedule);

                //vectorDB에 저장하기
                postNewAiSchedule(
                        postMetaDataScheduleDto.getInfo(),
                        postMetaDataScheduleDto.getLocation(),
                        postMetaDataScheduleDto.getPerson(),
                        postMetaDataScheduleDto.getStartTime(),
                        postMetaDataScheduleDto.getEndTime(),
                        category.getName(),
                        category.getId(),
                        category.getColor(),
                        newSchedule.getMember().getId(),
                        newSchedule.getId());

                chatService.createNewChatWithMetaData("\"" + postMetaDataScheduleDto.getInfo() + "\" " + "일정을 추가해드렸습니다! 관련 검색 결과를 추천해드릴게요. 원하는 내용을 스크랩해주세요:)",
                        ChatType.AI, 10, member, "{\"keyword\":\"" + postMetaDataScheduleDto.getKeyword() + "\",\"scheduleId\":" + newSchedule.getId() + "}"
                );
            }
            else{
                throw new NotFoundCategoryException();
            }
        } else {
            chatService.createNewChat("\"" + postMetaDataScheduleDto.getInfo() + "\" " + "일정 추가를 취소했습니다.\n더 필요한 것이 있으시면 알려주세요!", ChatType.AI, 1, member);
        }

        // 모든 채팅 내역 반환
        return chatService.getOneWeekUserChat(memberId);
    }

    /* 사용자가 직접 삭제한 스케쥴 */
    public GetScheduleListDto deleteSchedule(Long memberId, Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksBySchedule_Id(scheduleId);
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
        bookmarkRepository.deleteAll(bookmarks);
        scheduleRepository.delete(schedule);

        return getOneDayUserSchedule(memberId, scheduleTime);
    }

    /* 사용자가 AI가 삭제 요청한 스케쥴을 Accept/Deny한 여부에 따라서 채팅 및 스케쥴 저장 */
    public GetChatListDto deleteAiScheduleAccept(Long memberId, Boolean idAccepted, Long scheduleId){
        Member member = memberRepository.findMemberById(memberId);
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksBySchedule_Id(scheduleId);
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);

        /* 사용자가 Accept 했으면 스케쥴 생성하기 */
        if(idAccepted){
            /* 삭제할 스케쥴이 정상적으로 존재하는 경우 */
            if(schedule != null){
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

                String info = schedule.getInfo();

                //RDB에서 삭제
                bookmarkRepository.deleteAll(bookmarks);
                scheduleRepository.delete(schedule);

                chatService.createNewChat("\"" + info + "\" " + "일정을 삭제해드렸습니다!", ChatType.AI, 1, member);
            }
            else{
                throw new NotFoundScheduleException();
            }
        } else {
            chatService.createNewChat("일정 추가를 취소했습니다.\n다른 할 일이 있으면 알려주세요.", ChatType.AI, 1, member);
        }

        // 모든 채팅 내역 반환
        return chatService.getOneWeekUserChat(memberId);
    }

    /* 사용자가 직접 변경한 스케쥴을 RDB & VectorDB에 저장 */
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
        putAiSchedule(
                putScheduleDto.getInfo(),
                putScheduleDto.getLocation(),
                putScheduleDto.getPerson(),
                putScheduleDto.getStartTime(),
                putScheduleDto.getEndTime(),
                category.getName(),
                category.getId(),
                category.getColor(),
                memberId,
                putScheduleDto.getId());

        return getOneDayUserSchedule(memberId, putScheduleDto.getOriginalTime().withZoneSameInstant(ZoneId.of("Asia/Seoul")));
    }

    /* 새로운 스케쥴을 VectorDB에 저장하는 API 호출 */
    public void postNewAiSchedule(String info, String location, String person,
                                  ZonedDateTime startTime, ZonedDateTime endTime,
                                  String category, Long category_id, String category_color, Long memberId, Long scheduleId){

        // null 값은 전달되서는 안됨
        if(endTime == null){
            endTime = startTime;
        }

        // 서울 시간대로 VectorDB에 저장
        PostFastApiScheduleDto dto = PostFastApiScheduleDto.builder()
                .info(info)
                .location(location)
                .person(person)
                .startTime(startTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")))
                .endTime(endTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")))
                .category(category)
                .category_id(category_id)
                .category_color(category_color)
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

    /* 변경된 스케쥴을 VectorDB에 저장하는 API 호출 */
    public void putAiSchedule(String info, String location, String person,
                                  ZonedDateTime startTime, ZonedDateTime endTime,
                                  String category, Long category_id, String category_color, Long memberId, Long scheduleId){

        // null 값은 전달되서는 안됨
        if(endTime == null){
            endTime = startTime;
        }

        PutFastApiScheduleDto dto = PutFastApiScheduleDto.builder()
                .info(info)
                .location(location)
                .person(person)
                .startTime(startTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")))
                .endTime(endTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")))
                .category(category)
                .category_id(category_id)
                .category_color(category_color)
                .member_id(memberId)
                .schedule_id(scheduleId)
                .build();

        ResponseEntity<JsonNode> responseNode = fastApiPutScheduleApi.putFastApiSchedule(dto);

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

    public List<GetScheduleDto> makeScheduleWithCommentListDto(List<Schedule> scheduleList){
        // ScheduleListResponseDto에 매핑
        return scheduleList.stream()
                .map(schedule -> GetScheduleDto.builder()
                        .id(schedule.getId())
                        .category(schedule.getCategory().getName())
                        .categoryNum(schedule.getCategory().getId())
                        .categoryColor(schedule.getCategory().getColor())
                        .info(schedule.getInfo())
                        .startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime())
                        .nessComment(schedule.getTodo())
                        .details(GetScheduleDetailDto.builder()
                                .person(schedule.getPerson())
                                .location(schedule.getLocation())
                                .build())
                        .build())
                .toList();
    }
}
