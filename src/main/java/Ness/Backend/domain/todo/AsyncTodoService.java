package Ness.Backend.domain.todo;

import Ness.Backend.domain.schedule.ScheduleRepository;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.domain.todo.dto.request.PostFastApiTodoCategoryDto;
import Ness.Backend.domain.todo.dto.request.PostFastApiTodoDto;
import Ness.Backend.domain.todo.dto.request.PostFastApiTodoListDto;
import Ness.Backend.domain.todo.dto.response.PostFastApiRecommendListDto;
import Ness.Backend.global.fastApi.FastApiTodoApi;
import Ness.Backend.global.time.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
@Transactional
public class AsyncTodoService {
    private final FastApiTodoApi fastApiTodoApi;
    private final ScheduleRepository scheduleRepository;
    private final Time time;

    /* 일정 관련 한 줄 추천 가져오는 로직 */
    @Async
    public PostFastApiRecommendListDto getTodo(Long memberId){
        // 오늘 날짜 가져오기
        ZonedDateTime now = time.getToday();
        List<Schedule> upcomingSchedules = scheduleRepository.findUpcomingSchedulesByStart_Time(memberId, now);

        List<Schedule> filteredSchedules = upcomingSchedules.stream()
                .filter(schedule -> schedule.getTodo() == null)
                .toList();

        List<PostFastApiTodoDto> todoDtos = filteredSchedules.stream()
                .map(schedule -> PostFastApiTodoDto.builder()
                        .startTime(schedule.getStartTime())
                        .category(PostFastApiTodoCategoryDto.builder()
                                .id(schedule.getCategory().getId())
                                .name(schedule.getCategory().getName())
                                .color(schedule.getCategory().getColor())
                                .build())
                        .person(schedule.getPerson())
                        .location(schedule.getLocation())
                        .info(schedule.getInfo())
                        .build())
                .toList();

        PostFastApiTodoListDto userTodoList = new PostFastApiTodoListDto(todoDtos);

        PostFastApiRecommendListDto aiTodoList = fastApiTodoApi.creatFastApiTodo(userTodoList);

        aiTodoList.getRecommendationList()
                .forEach(todo -> todo.getTodo());

        return aiTodoList;
    }
}
