package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportTagRepository extends JpaRepository<ReportTag, Long> {
    //매 월 1일마다 특정 멤버가 생성한 보고서 태그 데이터를 가져오는 역할
    @Query( value = "SELECT * FROM report_tag " +
            "WHERE member_id = :memberId " +
            "AND EXTRACT(YEAR_MONTH FROM CONVERT_TZ(created_date, 'UTC', 'Asia/Seoul')) = EXTRACT(YEAR_MONTH FROM NOW()) " +
            "AND EXTRACT(DAY FROM CONVERT_TZ(created_date, 'UTC', 'Asia/Seoul')) = 1;",
            nativeQuery = true)
    List<ReportTag> findLastMonthReportTagByMember_Id(
            @Param("memberId") Long memberId);

    //List<ReportTag> findReportTagsByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(Long id, ZonedDateTime startOfMonth, ZonedDateTime now);
}
