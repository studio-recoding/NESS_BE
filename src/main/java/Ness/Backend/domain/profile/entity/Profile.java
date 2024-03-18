package Ness.Backend.domain.profile.entity;

import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    private String pictureUrl;

    private String nickname;

    private String name;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    @Builder
    public Profile(String pictureUrl, String nickname, String name, Member member){
        this.pictureUrl = pictureUrl;
        this.nickname = nickname;
        this.name = name;
        this.member = member;
    }
}
