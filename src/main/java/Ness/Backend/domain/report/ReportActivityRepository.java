package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportActivity;
import Ness.Backend.domain.report.entity.ReportMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportActivityRepository extends JpaRepository<ReportActivity, Long> {
    // 특정 맴버의 오늘 하루 생성된 데이터만 반환
    @Query( value = "SELECT * FROM report_activity " +
            "WHERE member_id = :memberId " +
            "AND DATE(created_date) = CURDATE() " +
            "ORDER BY created_date ASC",
            nativeQuery = true)
    List<ReportActivity> findTodayReportActivityByMember_Id(
            @Param("memberId") Long memberId);
}
