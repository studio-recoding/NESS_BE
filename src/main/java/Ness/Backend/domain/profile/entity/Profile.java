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

    private String pictureKey;

    private String nickname;

    private String name;

    private Boolean isEmailActive;

    //유저 페르소나를 구분해주는 타입 값
    @Enumerated(EnumType.STRING)
    private PersonaType personaType;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateProfile(String nickname, String pictureKey){
        this.nickname = nickname;
        if(pictureKey != null){
            this.pictureKey = pictureKey;
        }
    }

    public void updateMailActive(Boolean isEmailActive){
        this.isEmailActive = isEmailActive;
    }

    public void updatePersona(PersonaType personaType){
        this.personaType = personaType;
    }

    @Builder
    public Profile(String pictureUrl, String nickname, String name, Member member, Boolean isEmailActive, PersonaType personaType){
        this.pictureUrl = pictureUrl;
        this.nickname = nickname;
        this.name = name;
        this.member = member;
        this.isEmailActive = isEmailActive;
        this.personaType = personaType;
    }
}
