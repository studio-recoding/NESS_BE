package Ness.Backend.domain.bookmark.entity;

import Ness.Backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    private String contents;

    private ZonedDateTime datetime;

    private String title;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Builder
    public Bookmark(String contents, ZonedDateTime datetime, String title, String url, Schedule schedule){
        this.contents = contents;
        this.datetime = datetime;
        this.title = title;
        this.url = url;
        this.schedule = schedule;
    }
}
