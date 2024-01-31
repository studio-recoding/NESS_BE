package Ness.Backend.member;

import Ness.Backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 특정 맴버 ID로 맴버 엔티티 반환
    Member findMemberById(Long memberId);
}
