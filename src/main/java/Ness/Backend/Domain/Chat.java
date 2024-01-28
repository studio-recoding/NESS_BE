package Ness.Backend.Domain;

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
    private Member member;

}
