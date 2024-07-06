package Ness.Backend.domain.report;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.PersonaType;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.domain.report.dto.request.PostFastApiUserMemoryDto;
import Ness.Backend.domain.report.dto.request.PostFastApiUserRecommendActivityDto;
import Ness.Backend.domain.report.dto.request.PostFastApiUserTagDto;
import Ness.Backend.domain.report.dto.response.*;
import Ness.Backend.domain.report.entity.ReportActivity;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
import Ness.Backend.global.error.ErrorCode;
import Ness.Backend.global.error.exception.NotFoundException;
import Ness.Backend.global.fastApi.FastApiMemoryApi;
import Ness.Backend.global.fastApi.FastApiRecommendApi;
import Ness.Backend.global.fastApi.FastApiTagApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportService {
    private final ReportMemoryRepository reportMemoryRepository;
    private final ReportTagRepository reportTagRepository;
    private final ReportRecommendRepository reportRecommendRepository;
    private final ReportActivityRepository reportActivityRepository;
    private final FastApiRecommendApi fastApiRecommendApi;
    private final FastApiTagApi fastApiTagApi;
    private final FastApiMemoryApi fastApiMemoryApi;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    /* 메모리 가져오는 로직 */
    public GetReportMemoryListDto getMemory(Long memberId){
        log.info("getMemory called by member: " + memberId);

        // 오늘 날짜 가져오기
        ZonedDateTime now = getToday();

        List<ReportMemory> reportMemory = reportMemoryRepository.findTodayReportMemoryByMember_Id(memberId);

        if (reportMemory.isEmpty()){
            // 오늘치가 없다면 새롭게 생성하기
            String memory = postNewAiMemory(memberId, now);

            Member memberEntity = memberRepository.findMemberById(memberId);

            ReportMemory newMemory = ReportMemory.builder()
                    .createdDate(now)
                    .memory(memory)
                    .member(memberEntity)
                    .build();

            reportMemoryRepository.save(newMemory);
        }

        // 2주치의 데이터 가져오기
        List<ReportMemory> reportMemories = reportMemoryRepository.findTwoWeekUserMemoryByMember_Id(memberId);
        return createReportMemoryListDto(reportMemories);
    }

    public GetReportMemoryListDto createReportMemoryListDto(List<ReportMemory> reportMemories) {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        LocalDate startDate = ZonedDateTime.now(koreaZoneId).toLocalDate().minusDays(13);

        // 주어진 기간 내의 모든 날짜 리스트 생성
        List<LocalDate> allDates = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(14)
                .toList();

        List<GetReportMemoryDto> getReportMemoryDtos = allDates.stream()
                .map(date -> {
                    // 해당 날짜의 ReportMemory 객체를 찾음 (한국 시간대 기준으로 LocalDate 비교)
                    ReportMemory memory = reportMemories.stream()
                            .filter(m -> m.getCreatedDate().withZoneSameInstant(koreaZoneId).toLocalDate().equals(date))
                            .findFirst()
                            .orElse(null);

                    // ReportMemory 객체가 존재하면 해당 객체를 기반으로 DTO 생성, 없으면 임시 DTO 생성
                    return GetReportMemoryDto.builder()
                            .id(memory != null ? memory.getId() : 0) // 존재하지 않으면 ID는 0
                            .createdDate(memory != null ? memory.getCreatedDate().toString() : date.atStartOfDay(koreaZoneId).toString()) // 존재하지 않으면 해당 날짜의 00:00 KST 사용
                            .memory(memory != null ? memory.getMemory() : "\uD83C\uDE1A") // 임시 메모리 데이터
                            .build();
                })
                .toList();

        return new GetReportMemoryListDto(getReportMemoryDtos);
    }

    /*
    public GetReportMemoryListDto createReportMemoryListDto(List<ReportMemory> reportMemories) {
        //ReportMemoryListResponseDto에 매핑
        List<GetReportMemoryDto> getReportMemoryDtos = reportMemories.stream()
                .map(memory -> GetReportMemoryDto.builder()
                        .id(memory.getId())
                        .createdDate(memory.getCreatedDate().toString())
                        .memory(memory.getMemory())
                        .build())
                .toList();

        return new GetReportMemoryListDto(getReportMemoryDtos);
    }
     */

    public String postNewAiMemory(Long memberId, ZonedDateTime today){
        String persona = getPersona(memberId);
        PostFastApiUserMemoryDto userDto = PostFastApiUserMemoryDto.builder()
                .member_id(memberId.intValue())
                .user_persona(persona)
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하기
        PostFastApiAiMemoryDto aiDto = fastApiMemoryApi.creatFastApiMemory(userDto);

        return aiDto.getMemory();
    }

    /* 테그 가져오는 로직 */
    public GetReportTagListDto getTag(Long memberId){
        log.info("getTag called by member: " + memberId);

        // 오늘 날짜 가져오기
        ZonedDateTime now = getToday();

        List<ReportTag> reportTags = reportTagRepository.findLastMonthReportTagByMember_Id(memberId);

        if (reportTags == null || reportTags.isEmpty()) {
            PostFastApiAiTagListDto aiDto = postNewAiTag(memberId, getToday());

            Member memberEntity = memberRepository.findMemberById(memberId);

            for (PostFastApiAiTagDto tag : aiDto.getTags()) {
                ReportTag reportTag = ReportTag.builder()
                        .tagTitle(tag.getTitle())
                        .tagDesc(tag.getDesc())
                        .createdDate(now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                        .member(memberEntity)
                        .build();
                reportTagRepository.save(reportTag);
            }
            return createReportTagListDto(reportTagRepository.findLastMonthReportTagByMember_Id(memberId));
        } else {
            return createReportTagListDto(reportTags);
        }
    }

    public GetReportTagListDto createReportTagListDto(List<ReportTag> reportTags){
        List<GetReportTagDto> getReportTagDtos = reportTags.stream()
                .map(tag -> GetReportTagDto.builder()
                        .id(tag.getId())
                        .createdDate(tag.getCreatedDate().toString())
                        .tagTitle(tag.getTagTitle())
                        .tagDesc(tag.getTagDesc())
                        .build())
                .toList();

        return new GetReportTagListDto(getReportTagDtos);
    }

    public PostFastApiAiTagListDto getAiTag(Long memberId){
        return postNewAiTag(memberId, getToday());
    }

    public PostFastApiAiTagListDto postNewAiTag(Long memberId, ZonedDateTime today){
        String persona = getPersona(memberId);
        PostFastApiUserTagDto userDto = PostFastApiUserTagDto.builder()
                .member_id(memberId.intValue())
                .user_persona(persona)
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하고 값 받아오기
        return fastApiTagApi.creatFastApiTag(userDto);
    }

    /* 한 줄 추천 및 엑티비티 가져오는 로직 */
    public PostFastApiAiRecommendActivityDto getRecommendActivity(Long memberId){
        log.info("getRecommendActivity called by member: " + memberId);

        // 오늘 날짜 가져오기
        ZonedDateTime now = getToday();
        List<ReportRecommend> reportRecommends = reportRecommendRepository.findTodayReportRecommendByMember_Id(memberId);

        if(reportRecommends == null || reportRecommends.isEmpty()){
            //새로운 한 줄 추천 및 엑티비티 생성하기
            PostFastApiAiRecommendActivityDto aiDto = postNewAiRecommend(memberId, now);
            aiDto.setAnswer(parseAiRecommend(aiDto.getAnswer()));
            Member memberEntity = memberRepository.findMemberById(memberId);

            ReportRecommend newRecommend = ReportRecommend.builder()
                    .createdDate(now)
                    .recommendText(aiDto.getAnswer())
                    .member(memberEntity)
                    .build();
            
            //새롭게 생성된 한 줄 추천 저장하기
            reportRecommendRepository.save(newRecommend);

            //새롭게 생성된 활동 저장하기
            for (PostFastApiAiActivityDto activity : aiDto.getActivityList()) {
                ReportActivity reportActivity = ReportActivity.builder()
                        .activityText(activity.getActivity())
                        .imageTag(activity.getImageTag())
                        .createdDate(now)
                        .member(memberEntity)
                        .build();
                reportActivityRepository.save(reportActivity);
            }
            return aiDto;
        } else{
            ReportRecommend reportRecommend = reportRecommends.stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ErrorCode.MISMATCH_REPORT_RECOMMEND.getMessage()));

            List<ReportActivity> reportActivities = reportActivityRepository.findTodayReportActivityByMember_Id(memberId);

            List<PostFastApiAiActivityDto> postFastApiAiActivityDtos = reportActivities.stream()
                    .limit(3) // 처음 세 개의 원소만 선택
                    .map(activity -> PostFastApiAiActivityDto.builder()
                            .activity(activity.getActivityText())
                            .imageTag(activity.getImageTag())
                            .build())
                    .toList();

            PostFastApiAiRecommendActivityDto userDto = PostFastApiAiRecommendActivityDto.builder()
                    .answer(reportRecommend.getRecommendText())
                    .activityList(postFastApiAiActivityDtos)
                    .build();
            return userDto;
        }
    }

    public PostFastApiAiRecommendActivityDto postNewAiRecommend(Long memberId, ZonedDateTime today){
        String persona = getPersona(memberId);
        PostFastApiUserRecommendActivityDto userDto = PostFastApiUserRecommendActivityDto.builder()
                .member_id(memberId.intValue())
                .user_persona(persona)
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하기
        return fastApiRecommendApi.creatFastApiRecommend(userDto);
    }

    public String parseAiRecommend(String text){
        // AI에서 이 접두사를 붙여서 반환하는 경우가 많이 발생함
        text = text.replace("AI Recommendation: ", "");
        return text.replace("\"", "");
    }

    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public String getPersona(Long memberId){
        Profile profileEntity = profileRepository.findProfileByMember_Id(memberId);
        PersonaType personaType = profileEntity.getPersonaType();

        String persona = "default";
        if (personaType == PersonaType.HARDNESS){
            persona = "hard";
        }
        if (personaType == PersonaType.CALMNESS){
            persona = "calm";
        }

        return persona;
    }

}
