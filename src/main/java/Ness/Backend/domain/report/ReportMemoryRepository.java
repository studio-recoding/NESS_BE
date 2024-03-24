package Ness.Backend.domain.report;

import Ness.Backend.domain.report.entity.ReportMemory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReportMemoryRepository extends JpaRepository<ReportMemory, Long>{
    List<ReportMemory> findReportMemoriesByMember_idAndCreatedDateBetweenOrderByCreatedDateAsc(Long id, ZonedDateTime startOfWeek, ZonedDateTime now);
}
