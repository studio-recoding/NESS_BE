package Ness.Backend.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    private String info;

    private String location;

    private String person;

    @Embedded
    private ScheduleDate scheduleDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Builder
    public Schedule(Long id, String info, String location, String person, ScheduleDate scheduleDate,
                    Member member, Category category, Chat chat) {
        this.id = id;
        this.info = info;
        this.location = location;
        this.person = person;
        this.scheduleDate = scheduleDate;
        this.member = member;
        this.category = category;
        this.chat = chat;
    }

}
