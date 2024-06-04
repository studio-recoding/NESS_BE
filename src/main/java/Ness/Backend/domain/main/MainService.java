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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {
    private final ReportService reportService;
    private final TodoService todoService;
    public GetMainDto getMain(Long memberId){
        try {
            CompletableFuture<List<GetScheduleDto>> scheduleFuture = CompletableFuture.supplyAsync(() -> todoService.getTodo(memberId));
            CompletableFuture<PostFastApiAiRecommendActivityDto> activityFuture = CompletableFuture.supplyAsync(() -> reportService.getRecommendActivity(memberId));
            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(scheduleFuture, activityFuture);

            // 두 작업이 모두 완료될 때까지 기다린다.(각각 진행되는 것은 맞음, 가장 늦은 작업 완료에 맞춤)
            combinedFuture.join();
            List<GetScheduleDto> scheduleDtos = scheduleFuture.get();
            PostFastApiAiRecommendActivityDto activityDtos = activityFuture.get();
            return toEntity(scheduleDtos, activityDtos);

        } catch (Exception exception){
            List<GetScheduleDto> scheduleDtos = todoService.getTodo(memberId);
            PostFastApiAiRecommendActivityDto activityDtos = reportService.getRecommendActivity(memberId);
            return toEntity(scheduleDtos, activityDtos);
        }
    }

    //Entity -> DTO
    public GetMainDto toEntity(List<GetScheduleDto> scheduleDtos, PostFastApiAiRecommendActivityDto activityDtos){
        return GetMainDto.builder()
                .recommendText(activityDtos.getAnswer())
                .activityList(activityDtos.getActivityList())
                .scheduleList(scheduleDtos)
                .build();
    }

}
