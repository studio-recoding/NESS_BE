package Ness.Backend.domain.schedule.entity;

import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

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

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Builder
    public Schedule(Long id, String info, String location, String person, ZonedDateTime startTime, ZonedDateTime endTime,
                    Member member, Category category, Chat chat) {
        this.id = id;
        this.info = info;
        this.location = location;
        this.person = person;
        this.startTime = startTime;
        this.endTime = endTime;
        this.member = member;
        this.category = category;
        this.chat = chat;
    }

    public void changeSchedule(String info, String location, String person, ZonedDateTime startTime, ZonedDateTime endTime, Category category){
        this.info = info;
        this.location = location;
        this.person = person;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }
}
