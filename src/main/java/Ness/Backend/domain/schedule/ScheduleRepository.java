package Ness.Backend.domain.schedule;

import Ness.Backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
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

    // 특정 맴버의 하루 치 스케쥴 반환(ZoneDateTime 사용)
    @Query( value = "SELECT * FROM schedule " +
            "WHERE member_id = :memberId " +
            "AND DATE(CONVERT_TZ(start_time, '+00:00', '+09:00')) = DATE(CONVERT_TZ(:date, '+00:00', '+09:00')) " +
            "ORDER BY start_time ASC",
            nativeQuery = true)
    List<Schedule> findOneDaySchedulesByMember_Id(
        @Param("memberId") Long memberId,
        @Param("date") ZonedDateTime date);

    //스케쥴 ID로 특정 스케쥴 찾아주기
    Schedule findScheduleById(Long scheduleId);
}
