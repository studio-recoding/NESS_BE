package Ness.Backend.domain.chat.entity;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Chat {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ZonedDateTime createdDate;

    private String text;

    private int caseNumber;

    //AI 발화인지, USER 발화인지 구분해주는 타입 값
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @OneToOne(mappedBy = "chat", fetch = FetchType.LAZY)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Chat(Long id, ZonedDateTime createdDate, String text, ChatType chatType,
                int caseNumber, Schedule schedule, Member member) {
        this.id = id;
        this.createdDate = createdDate;
        this.text = text;
        this.chatType = chatType;
        this.caseNumber = caseNumber;
        this.schedule = schedule;
        this.member = member;
    }
}
