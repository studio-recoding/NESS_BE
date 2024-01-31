package Ness.Backend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Chat {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    private String text;

    //AI 발화인지, USER 발화인지 구분해주는 타입 값
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @OneToOne(mappedBy = "chat")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Chat(Long id, LocalDateTime createdDate, String text, ChatType chatType, Schedule schedule, Member member) {
        this.id = id;
        this.createdDate = createdDate;
        this.text = text;
        this.chatType = chatType;
        this.schedule = schedule;
        this.member = member;
    }
}
