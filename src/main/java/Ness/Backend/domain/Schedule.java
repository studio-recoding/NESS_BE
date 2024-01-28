package Ness.Backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Schedule {
    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private String id;

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

}
