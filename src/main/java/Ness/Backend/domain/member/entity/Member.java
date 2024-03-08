package Ness.Backend.domain.entity;


import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.profile.entity.Profile;
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

    @Column(unique = true)
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

    @Builder
    public Member(String email, String password, MemberRole memberRole){
        this.email = email;
        this.password = password;
        this.memberRole = Objects.requireNonNullElse(memberRole, memberRole.ROLE_USER); //값이 없다면, ROLE_USER로 초기화
    }
}
