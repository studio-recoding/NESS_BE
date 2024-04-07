package Ness.Backend.domain.report;

import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.response.PostFastApiAiChatDto;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.report.dto.request.PostFastApiUserRecommendDto;
import Ness.Backend.domain.report.dto.response.*;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
import Ness.Backend.global.fastApi.FastApiRecommendApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
    private final MemberRepository memberRepository;

    public GetReportMemoryListDto getMemory(Long id){
        // 2주치의 데이터 가져오기
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime startOfWeek = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime startOfLastWeek = startOfWeek.minusWeeks(1);

        List<ReportMemory> reportMemories = reportMemoryRepository.findReportMemoriesByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(id, startOfLastWeek, now);

        //ReportMemoryListResponseDto에 매핑
        List<GetReportMemoryDto> getReportMemoryDtos = reportMemories.stream()
                .map(memory -> GetReportMemoryDto.builder()
                        .id(memory.getId())
                        .createdDate(memory.getCreatedDate().toString())
                        .pictureUrl(memory.getPictureUrl())
                        .build())
                .toList();

        return new GetReportMemoryListDto(getReportMemoryDtos);
    }

    public GetReportTagListDto getTag(Long id){
        // 지난 달 10일~이번 달 9일 간의 데이터 가져오기
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime lastMonth10 = now.withMonth(now.getMonthValue() - 1).withDayOfMonth(10).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime thisMonth9 = now.withMonth(now.getMonthValue()).withDayOfMonth(9).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<ReportTag> reportTags = reportTagRepository.findReportTagsByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(id, lastMonth10, thisMonth9);

        //ReportTagListResponseDto에 매핑
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

    public GetReportRecommendDto getRecommend(Long id){
        // 이번 달의 데이터 가져오기
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime today = now.toLocalDate().atStartOfDay(now.getZone());

        ReportRecommend reportRecommend = reportRecommendRepository.findTodayReportRecommendByMember_Id(id);

        if(reportRecommend == null){
            //새로운 한 줄 추천 생성하기
            String answer = postNewAiRecommend(id, today);
            String parsedAnswer = parseAiRecommend(answer);

            Member memberEntity = memberRepository.findMemberById(id);

            ReportRecommend newRecommend = ReportRecommend.builder()
                    .createdDate(today)
                    .recommendText(answer)
                    .member(memberEntity)
                    .build();
            
            //새롭게 생성된 한 줄 추천 저장하기
            reportRecommendRepository.save(newRecommend);

            return GetReportRecommendDto.builder()
                    .id(newRecommend.getId())
                    .createdDate(newRecommend.getCreatedDate().toString())
                    .recommendText(newRecommend.getRecommendText())
                    .build();
        } else{
            return GetReportRecommendDto.builder()
                    .id(reportRecommend.getId())
                    .createdDate(reportRecommend.getCreatedDate().toString())
                    .recommendText(reportRecommend.getRecommendText())
                    .build();
        }
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
        PostFastApiAiRecommendDto AiDto = fastApiRecommendApi.creatFastApiRecommend(userDto);

        return AiDto.getAnswer();
    }
}
