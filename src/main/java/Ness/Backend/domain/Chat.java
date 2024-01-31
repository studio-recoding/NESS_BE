package Ness.Backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Chat {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue
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

}
