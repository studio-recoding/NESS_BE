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
            "AND DATE(CONVERT_TZ(created_date, '+00:00', '+09:00')) = DATE(CONVERT_TZ(NOW(), '+00:00', '+09:00')) " +
            "ORDER BY created_date ASC",
            nativeQuery = true)
    List<ReportMemory> findTodayReportMemoryByMember_Id(
            @Param("memberId") Long memberId);


    // 특정 맴버의 2주치 생성된 데이터 반환(단, 하루에 만드시 한개씩 반환, 2주치면 최대 14개)
    @Query(value = "SELECT * FROM ( " +
            "SELECT report_memory.*, ROW_NUMBER() OVER (PARTITION BY DATE(CONVERT_TZ(created_date, '+00:00', '+09:00')) " +
            "ORDER BY created_date DESC) as row_num " +
            "FROM report_memory " +
            "WHERE member_id = :memberId " +
            "AND created_date >= CONVERT_TZ(DATE_SUB(NOW(), INTERVAL 2 WEEK), '+00:00', '+09:00') " +
            ") AS subquery " +
            "WHERE row_num = 1 " +
            "ORDER BY created_date ASC " +
            "LIMIT 14",
            nativeQuery = true)
    List<ReportMemory> findTwoWeekUserMemoryByMember_Id(
            @Param("memberId") Long memberId);

}
