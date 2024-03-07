package Ness.Backend.profile.entity;

import Ness.Backend.entity.Member;
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

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Profile(String pictureUrl, Member member){
        this.pictureUrl = pictureUrl;
        this.member = member;
    }
}
