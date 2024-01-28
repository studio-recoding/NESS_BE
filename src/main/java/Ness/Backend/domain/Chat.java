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

    @OneToOne(mappedBy = "chat")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
