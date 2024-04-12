package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReportTagRepository extends JpaRepository<ReportTag, Long> {
    @Query( value = "SELECT * FROM report_tag " +
            "WHERE member_id = :memberId " +
            "AND EXTRACT(YEAR_MONTH FROM created_date) = EXTRACT(YEAR_MONTH FROM NOW()) " +
            "AND EXTRACT(DAY FROM created_date) = 1;",
            nativeQuery = true)
    List<ReportTag> findLastMonthReportTagByMember_Id(
            @Param("memberId") Long memberId);

    //List<ReportTag> findReportTagsByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(Long id, ZonedDateTime startOfMonth, ZonedDateTime now);
}
