package Ness.Backend.domain.member;

import Ness.Backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 특정 맴버 ID로 맴버 엔티티 반환
    Member findMemberById(Long memberId);

    // 특정 맴버 이메일로 맴버 엔티티 반환
    Member findMemberByEmail(String email);

    boolean existsByEmail(String email);
    
    // 이메일 전송 기능 활성화된 멤버 반환
    List<Member> findMembersByProfileIsEmailActive(Boolean isActive);
}
