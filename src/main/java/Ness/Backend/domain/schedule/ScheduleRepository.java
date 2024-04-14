package Ness.Backend.domain.schedule;

import Ness.Backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 특정 맴버의 한달 치 스케쥴 반환
    @Query( value = "SELECT * FROM schedule " +
                    "WHERE member_id = :memberId " +
                    "AND YEAR(start_time) = :year " +
                    "AND MONTH(start_time) = :month " +
                    "ORDER BY start_time ASC",
            nativeQuery = true)
    List<Schedule> findOneMonthSchedulesByMember_Id(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month);

    //스케쥴 ID로 특정 스케쥴 찾아주기
    Schedule findScheduleById(Long scheduleId);
}
