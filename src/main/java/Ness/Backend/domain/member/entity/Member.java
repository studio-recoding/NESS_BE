package Ness.Backend.domain.member.entity;


import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.domain.report.entity.ReportMemory;
import Ness.Backend.domain.report.entity.ReportRecommend;
import Ness.Backend.domain.report.entity.ReportTag;
import Ness.Backend.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @OneToOne(mappedBy = "member")
    private Profile profile;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ReportTag> reportTags = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ReportMemory> reportMemories = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ReportRecommend> reportRecommends = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    public void updateIsDeleted() {
        this.isDeleted = !this.isDeleted;
    }

    @Builder
    public Member(String email, String password, MemberRole memberRole){
        this.email = email;
        this.password = password;
        this.memberRole = Objects.requireNonNullElse(memberRole, memberRole.ROLE_USER); //값이 없다면, ROLE_USER로 초기화
    }
}
