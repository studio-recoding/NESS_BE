package Ness.Backend.domain.schedule;

import Ness.Backend.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 특정 맴버 ID로 스케줄 리스트 반환
    List<Schedule> findByMember_Id(Long memberId);
}
