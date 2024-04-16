package Ness.Backend.domain.main;

import Ness.Backend.domain.main.dto.response.GetMainDto;
import Ness.Backend.domain.report.ReportRecommendRepository;
import Ness.Backend.domain.report.ReportService;
import Ness.Backend.domain.report.dto.response.GetReportRecommendDto;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.schedule.ScheduleRepository;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDetailDto;
import Ness.Backend.domain.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {
    private final ScheduleRepository scheduleRepository;
    private final ReportRecommendRepository reportRecommendRepository;
    private final ReportService reportService;

    public GetMainDto getMain(Long id){
        Schedule schedule = scheduleRepository.findTodayOneScheduleByMember_Id(id);
        GetReportRecommendDto getReportRecommendDto = reportService.getRecommend(id);

        return Optional.ofNullable(schedule)
                .map(s -> GetMainDto.builder()
                        .recommendId(getReportRecommendDto.getId())
                        .recommendText(getReportRecommendDto.getRecommendText())
                        .scheduleId(s.getId())
                        .category(s.getCategory().getName())
                        .categoryNum(s.getCategory().getId())
                        .info(s.getInfo())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .details(GetScheduleDetailDto.builder()
                                .person(s.getPerson())
                                .location(s.getLocation())
                                .build())
                        .build())
                .orElse(GetMainDto.builder()
                        .recommendId(getReportRecommendDto.getId())
                        .recommendText(getReportRecommendDto.getRecommendText())
                        .scheduleId(null)
                        .category(null)
                        .categoryNum(null)
                        .info(null)
                        .startTime(null)
                        .endTime(null)
                        .details(null)
                        .build());
    }
}
