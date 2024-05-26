package Ness.Backend.domain.main;

import Ness.Backend.domain.main.dto.response.GetMainDto;
import Ness.Backend.domain.todo.AsyncTodoService;
import Ness.Backend.domain.todo.dto.response.PostFastApiRecommendListDto;
import Ness.Backend.domain.report.AsyncReportService;
import Ness.Backend.domain.report.dto.response.PostFastApiAiRecommendActivityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {
    private final AsyncReportService asyncReportService;
    private final AsyncTodoService asyncTodoService;

    public GetMainDto getMain(Long memberId){
        PostFastApiAiRecommendActivityDto activityDto = asyncReportService.getRecommendActivity(memberId);
        PostFastApiRecommendListDto todoDto = asyncTodoService.getTodo(memberId);

        return GetMainDto.builder()
                .recommendText(activityDto.getAnswer())
                .activityList(activityDto.getActivityList())
                .build();
    }
}
