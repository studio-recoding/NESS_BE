package Ness.Backend.domain.report;

import Ness.Backend.domain.chat.dto.ChatDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.report.dto.*;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportMemoryRepository reportMemoryRepository;
    private final ReportTagRepository reportTagRepository;
    private final ReportRecommendRepository reportRecommendRepository;

    public ReportMemoryListResponseDto getMemory(Long id){
        // 이번 주의 데이터 가져오기
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime startOfWeek = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);

        //ZonedDateTime oneWeekAgo = now.minus(7, ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<ReportMemory> reportMemories = reportMemoryRepository.findReportMemoriesByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(id, startOfWeek, now);

        //ReportMemoryListResponseDto에 매핑
        List<ReportMemoryDto> reportMemoryDtos = reportMemories.stream()
                .map(memory -> ReportMemoryDto.builder()
                        .id(memory.getId())
                        .createdDate(memory.getCreatedDate().toString())
                        .pictureUrl(memory.getPictureUrl())
                        .build())
                .toList();

        return new ReportMemoryListResponseDto(reportMemoryDtos);
    }

    public ReportTagListResponseDto getTag(Long id){
        // 이번 달의 데이터 가져오기
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<ReportTag> reportTags = reportTagRepository.findReportTagsByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(id, startOfMonth, now);

        //ReportTagListResponseDto에 매핑
        List<ReportTagDto> reportTagDtos = reportTags.stream()
                .map(tag -> ReportTagDto.builder()
                        .id(tag.getId())
                        .createdDate(tag.getCreatedDate().toString())
                        .tagTitle(tag.getTagTitle())
                        .tagDesc(tag.getTagDesc())
                        .build())
                .toList();

        return new ReportTagListResponseDto(reportTagDtos);
    }

    public ReportRecommendResponseDto getRecommend(Long id){
        // 이번 달의 데이터 가져오기
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime startOfToday = now.toLocalDate().atStartOfDay(now.getZone());
        ZonedDateTime endOfToday = now.toLocalDate().atTime(LocalTime.MAX).atZone(now.getZone());

        ReportRecommend reportRecommend = reportRecommendRepository.findReportRecommendByMember_IdAndCreatedDateBetween(id, startOfToday, endOfToday);

        return ReportRecommendResponseDto.builder()
                .id(reportRecommend.getId())
                .createdDate(reportRecommend.getCreatedDate().toString())
                .recommendText(reportRecommend.getRecommendText())
                .build();
    }
}
