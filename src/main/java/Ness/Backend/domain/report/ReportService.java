package Ness.Backend.domain.report;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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

    /* 메모리 가져오는 로직 */
    public GetReportMemoryListDto getMemory(Long id){
        log.info("getMemory called by member: " + id);

        // 오늘 날짜 가져오기
        ZonedDateTime now = getToday();

        ReportMemory reportMemory = reportMemoryRepository.findTodayReportMemoryByMember_Id(id);

        if (reportMemory == null){
            // 오늘치가 없다면 새롭게 생성하기
            String memory = postNewAiMemory(id, now);

            Member memberEntity = memberRepository.findMemberById(id);

            ReportMemory newMemory = ReportMemory.builder()
                    .createdDate(now)
                    .memory(memory)
                    .member(memberEntity)
                    .build();

            reportMemoryRepository.save(newMemory);
        }

        // 2주치의 데이터 가져오기
        List<ReportMemory> reportMemories = reportMemoryRepository.findTwoWeekUserMemoryByMember_Id(id);
        return createReportMemoryListDto(reportMemories);
    }

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

    public String postNewAiMemory(Long id, ZonedDateTime today){
        PostFastApiUserMemoryDto userDto = PostFastApiUserMemoryDto.builder()
                .member_id(id.intValue())
                .user_persona("")
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하기
        PostFastApiAiMemoryDto aiDto = fastApiMemoryApi.creatFastApiMemory(userDto);

        return aiDto.getMemory();
    }

    /* 테그 가져오는 로직 */
    public GetReportTagListDto getTag(Long id){
        log.info("getTag called by member: " + id);

        // 오늘 날짜 가져오기
        ZonedDateTime now = getToday();

        List<ReportTag> reportTags = reportTagRepository.findLastMonthReportTagByMember_Id(id);

        if (reportTags == null || reportTags.isEmpty()) {
            PostFastApiAiTagListDto aiDto = postNewAiTag(id, getToday());

            Member memberEntity = memberRepository.findMemberById(id);

            for (PostFastApiAiTagDto tag : aiDto.getTags()) {
                ReportTag reportTag = ReportTag.builder()
                        .tagTitle(tag.getTitle())
                        .tagDesc(tag.getDesc())
                        .createdDate(now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                        .member(memberEntity)
                        .build();
                reportTagRepository.save(reportTag);
            }
            return createReportTagListDto(reportTagRepository.findLastMonthReportTagByMember_Id(id));
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

    public PostFastApiAiTagListDto getAiTag(Long id){
        return postNewAiTag(id, getToday());
    }

    public PostFastApiAiTagListDto postNewAiTag(Long id, ZonedDateTime today){
        PostFastApiUserTagDto userDto = PostFastApiUserTagDto.builder()
                .member_id(id.intValue())
                .user_persona("")
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

        if(reportRecommends.isEmpty()){
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

    public PostFastApiAiRecommendActivityDto postNewAiRecommend(Long id, ZonedDateTime today){
        PostFastApiUserRecommendActivityDto userDto = PostFastApiUserRecommendActivityDto.builder()
                .member_id(id.intValue())
                .user_persona("") // 빈 문자열은 default
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하기
        return fastApiRecommendApi.creatFastApiRecommend(userDto);
    }

    public String parseAiRecommend(String text){
        return text.replace("\"", "");
    }

    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}
