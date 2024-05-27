package Ness.Backend.domain.main;

import Ness.Backend.domain.main.dto.response.GetMainDto;
import Ness.Backend.domain.report.ReportService;
import Ness.Backend.domain.report.dto.response.PostFastApiAiRecommendActivityDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDto;
import Ness.Backend.domain.todo.TodoService;
import Ness.Backend.domain.todo.dto.response.PostFastApiRecommendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {
    private final ReportService reportService;
    private final TodoService todoService;

    public GetMainDto getMain(Long memberId){
        List<GetScheduleDto> scheduleDtos = todoService.getTodo(memberId);
        PostFastApiAiRecommendActivityDto activityDtos = reportService.getRecommendActivity(memberId);

        return GetMainDto.builder()
                .recommendText(activityDtos.getAnswer())
                .activityList(activityDtos.getActivityList())
                .scheduleList(scheduleDtos)
                .build();
    }
}
