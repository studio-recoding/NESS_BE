package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReportTagRepository extends JpaRepository<ReportTag, Long> {
    List<ReportTag> findReportTagsByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(Long id, ZonedDateTime startOfMonth, ZonedDateTime now);
}
