package Ness.Backend.domain.report;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.report.dto.request.PostFastApiUserMemoryDto;
import Ness.Backend.domain.report.dto.request.PostFastApiUserRecommendDto;
import Ness.Backend.domain.report.dto.request.PostFastApiUserTagDto;
import Ness.Backend.domain.report.dto.response.*;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
import Ness.Backend.global.fastApi.FastApiMemoryApi;
import Ness.Backend.global.fastApi.FastApiRecommendApi;
import Ness.Backend.global.fastApi.FastApiTagApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportMemoryRepository reportMemoryRepository;
    private final ReportTagRepository reportTagRepository;
    private final ReportRecommendRepository reportRecommendRepository;
    private final FastApiRecommendApi fastApiRecommendApi;
    private final FastApiTagApi fastApiTagApi;
    private final FastApiMemoryApi fastApiMemoryApi;
    private final MemberRepository memberRepository;

    public GetReportMemoryListDto getMemory(Long id){
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
    public PostFastApiAiTagListDto getAiTag(Long id){
         return postNewAiTag(id, getToday());
    }

    public GetReportTagListDto getTag(Long id){
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

    public GetReportRecommendDto getRecommend(Long id){
        // 오늘 날짜 가져오기
        ZonedDateTime now = getToday();

        ReportRecommend reportRecommend = reportRecommendRepository.findTodayReportRecommendByMember_Id(id);

        if(reportRecommend == null){
            //새로운 한 줄 추천 생성하기
            String answer = postNewAiRecommend(id, now);
            String parsedAnswer = parseAiRecommend(answer);

            Member memberEntity = memberRepository.findMemberById(id);

            ReportRecommend newRecommend = ReportRecommend.builder()
                    .createdDate(now)
                    .recommendText(parsedAnswer)
                    .member(memberEntity)
                    .build();
            
            //새롭게 생성된 한 줄 추천 저장하기
            reportRecommendRepository.save(newRecommend);

            return createReportRecommendDto(newRecommend.getId(), newRecommend.getCreatedDate().toString(), newRecommend.getRecommendText());
        } else{
            return createReportRecommendDto(reportRecommend.getId(), reportRecommend.getCreatedDate().toString(), reportRecommend.getRecommendText());
        }
    }

    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
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

    public GetReportRecommendDto createReportRecommendDto(Long id, String date, String text){
        return GetReportRecommendDto.builder()
                .id(id)
                .createdDate(date)
                .recommendText(text)
                .build();
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

    public String parseAiRecommend(String text){
        return text.replace("\"", "");
    }

    public String postNewAiRecommend(Long id, ZonedDateTime today){
        PostFastApiUserRecommendDto userDto = PostFastApiUserRecommendDto.builder()
                .member_id(id.intValue())
                .user_persona("")
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하기
        PostFastApiAiRecommendDto aiDto = fastApiRecommendApi.creatFastApiRecommend(userDto);

        return aiDto.getAnswer();
    }
}
