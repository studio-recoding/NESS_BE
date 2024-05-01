package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{

    // 특정 맴버의 일주일치 데이터만 반환
    @Query( value = "SELECT * FROM chat " +
            "WHERE member_id = :memberId " +
            "AND created_date >= DATE_SUB(NOW(), INTERVAL 1 WEEK) " +
            //"AND created_date BETWEEN DATE_ADD(NOW(), INTERVAL -1 WEEK) AND NOW() " +
            "ORDER BY created_date ASC",
            nativeQuery = true)
    List<Chat> findOneWeekUserChatsByMember_Id(
            @Param("memberId") Long memberId);

    Chat findChatById(Long memberId);
}
