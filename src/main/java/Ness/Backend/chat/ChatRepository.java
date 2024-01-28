package Ness.Backend.chat;

import Ness.Backend.domain.Chat;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{

    // 특정 member_id를 가진 모든 Chat을 찾기 위한 메서드
    List<Chat> findByMemberId(Long memberId);

    //모든 Chat의 List를 반환
    List<Chat> findAll();
}
