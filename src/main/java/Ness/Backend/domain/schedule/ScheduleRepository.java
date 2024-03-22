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
    // 특정 맴버 ID로 스케줄 리스트 반환
    List<Schedule> findByMember_Id(Long memberId);

    @Query( value = "SELECT * FROM schedule " +
                    "WHERE member_id = :memberId " +
                    "AND YEAR(date) = :year " +
                    "AND MONTH(date) = :month " +
                    "ORDER BY date ASC",
            nativeQuery = true)
    List<Schedule> findSchedulesByMember_IdAndMonthOrderByDateAsc(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month);

    @Query( value = "SELECT * FROM schedule " +
            "WHERE member_id = :memberId " +
            "AND YEAR(date) = :year " +
            "AND MONTH(date) = :month " +
            "AND DATE(date) = :day " +
            "ORDER BY date ASC",
            nativeQuery = true)
    List<Schedule> findSchedulesByMember_IdAndDayOrderByDateAsc(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("day") int day);
}
