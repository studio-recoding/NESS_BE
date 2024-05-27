package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReportMemoryRepository extends JpaRepository<ReportMemory, Long>{
    // 특정 맴버의 오늘 하루 생성된 데이터만 반환
    @Query( value = "SELECT * FROM report_memory " +
            "WHERE member_id = :memberId " +
            "AND DATE(CONVERT_TZ(created_date, '+00:00', '+09:00')) = CURDATE() " +
            "ORDER BY created_date ASC",
            nativeQuery = true)
    ReportMemory findTodayReportMemoryByMember_Id(
            @Param("memberId") Long memberId);

    @Query( value = "SELECT * FROM report_memory " +
            "WHERE member_id = :memberId " +
            "AND created_date >= DATE_SUB(NOW(), INTERVAL 2 WEEK) " +
            "ORDER BY created_date ASC",
            nativeQuery = true)
    List<ReportMemory> findTwoWeekUserMemoryByMember_Id(
            @Param("memberId") Long memberId);

    List<ReportMemory> findReportMemoriesByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(Long id, ZonedDateTime startOfWeek, ZonedDateTime now);
}
