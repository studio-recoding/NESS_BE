package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRecommendRepository extends JpaRepository<ReportRecommend, Long> {
    // 특정 맴버의 오늘 하루 생성된 데이터만 반환
    @Query( value = "SELECT * FROM report_recommend " +
            "WHERE member_id = :memberId " +
            "AND DATE(created_date) = CURDATE() " +
            "ORDER BY created_date ASC",
            nativeQuery = true)
    ReportRecommend findTodayReportRecommendByMember_Id(
            @Param("memberId") Long memberId);

    //ReportRecommend findReportRecommendByMember_IdAndCreatedDateBetween(Long id, ZonedDateTime startOfDay, ZonedDateTime now);
}
