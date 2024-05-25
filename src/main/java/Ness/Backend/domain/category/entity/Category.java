package Ness.Backend.domain.category.entity;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.member.entity.MemberRole;
import Ness.Backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    //카테고리별 색상
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "category")
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    public Category(Member member, String name, String color){
        this.member = member;
        this.name = name;
        this.color = color;
    }
}
