package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;

public interface ReportRecommendRepository extends JpaRepository<ReportRecommend, Long> {
    ReportRecommend findReportRecommendByMember_IdAndCreatedDateBetween(Long id, ZonedDateTime startOfDay, ZonedDateTime now);
}
