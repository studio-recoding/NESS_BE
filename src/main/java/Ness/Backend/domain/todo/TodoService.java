package Ness.Backend.domain.todo;

import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.domain.schedule.ScheduleRepository;
import Ness.Backend.domain.schedule.ScheduleService;
import Ness.Backend.domain.schedule.dto.response.GetScheduleDto;
import Ness.Backend.domain.schedule.dto.response.GetScheduleListDto;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.domain.todo.dto.request.PostFastApiTodoCategoryDto;
import Ness.Backend.domain.todo.dto.request.PostFastApiTodoDto;
import Ness.Backend.domain.todo.dto.request.PostFastApiTodoListDto;
import Ness.Backend.domain.todo.dto.response.PostFastApiRecommendListDto;
import Ness.Backend.global.fastApi.FastApiTodoApi;
import Ness.Backend.global.time.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoService {
    private final FastApiTodoApi fastApiTodoApi;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final ProfileRepository profileRepository;
    private final Time time;

    /* 일정 관련 한 줄 추천 가져오는 로직 */
    public List<GetScheduleDto> getTodo(Long memberId){
        log.info("getTodo called by member: " + memberId);

        Profile userProfile = profileRepository.findProfileByMember_Id(memberId);
        // 오늘 날짜 가져오기
        ZonedDateTime now = time.getToday();
        List<Schedule> upcomingSchedules = scheduleRepository.findUpcomingSchedulesByStart_Time(memberId, now);

        List<Schedule> filteredSchedules = upcomingSchedules.stream()
                .filter(schedule -> schedule.getTodo() == null)
                .toList();

        if(!filteredSchedules.isEmpty()){
            List<PostFastApiTodoDto> todoDtos = filteredSchedules.stream()
                    .map(schedule -> PostFastApiTodoDto.builder()
                            .id(schedule.getId())
                            .startTime(schedule.getStartTime())
                            .category(PostFastApiTodoCategoryDto.builder()
                                    .id(schedule.getCategory().getId())
                                    .name(schedule.getCategory().getName())
                                    .color(schedule.getCategory().getColor())
                                    .build())
                            .person(schedule.getPerson() != null ? schedule.getPerson() : "")
                            .location(schedule.getLocation() != null ? schedule.getLocation() : "")
                            .info(schedule.getInfo() != null ? schedule.getInfo() : "")
                            .build())
                    .toList();

            PostFastApiTodoListDto userTodoList = PostFastApiTodoListDto.builder()
                    .persona(userProfile.getPersonaType())
                    .todoList(todoDtos)
                    .build();

            PostFastApiRecommendListDto aiTodoList = fastApiTodoApi.creatFastApiTodo(userTodoList);

            //AI에서 받아온 TODO를 업데이트
            aiTodoList.getRecommendationList()
                    .forEach(todo -> scheduleRepository
                            .findScheduleById(todo.getTodo().getId())
                            .updateTodo(todo.getNessComment()));
        }

        List<Schedule> updatedSchedules = scheduleRepository.findUpcomingSchedulesByStart_Time(memberId, now);

        return scheduleService.makeScheduleWithCommentListDto(updatedSchedules);
    }
}
