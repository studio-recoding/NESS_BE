package Ness.Backend.domain.report;

import Ness.Backend.domain.report.dto.response.*;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
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
        ZonedDateTime startOfToday = now.toLocalDate().atStartOfDay(now.getZone());
        ZonedDateTime endOfToday = now.toLocalDate().atTime(LocalTime.MAX).atZone(now.getZone());

        ReportRecommend reportRecommend = reportRecommendRepository.findReportRecommendByMember_IdAndCreatedDateBetween(id, startOfToday, endOfToday);

        return GetReportRecommendDto.builder()
                .id(reportRecommend.getId())
                .createdDate(reportRecommend.getCreatedDate().toString())
                .recommendText(reportRecommend.getRecommendText())
                .build();
    }
}
