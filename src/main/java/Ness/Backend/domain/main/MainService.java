package Ness.Backend.domain.main;

import Ness.Backend.domain.main.dto.response.GetMainDto;
import Ness.Backend.domain.report.ReportService;
import Ness.Backend.domain.report.dto.response.PostFastApiAiRecommendActivityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {
    private final ReportService reportService;

    public GetMainDto getMain(Long id){
        PostFastApiAiRecommendActivityDto dto = reportService.getRecommendActivity(id);
        return GetMainDto.builder()
                .recommendText(dto.getAnswer())
                .activityList(dto.getActivityList())
                .build();
    }
}
