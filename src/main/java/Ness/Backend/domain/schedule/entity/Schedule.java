package Ness.Backend.domain.schedule.entity;

import Ness.Backend.domain.bookmark.entity.Bookmark;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToMany(mappedBy = "schedule")
    private List<Bookmark> bookmarks = new ArrayList<>();

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

    public void changeCategory(Category category){
        this.category = category;
    }

    public void updateTodo(String todo){
        this.todo = todo;
    }
}
