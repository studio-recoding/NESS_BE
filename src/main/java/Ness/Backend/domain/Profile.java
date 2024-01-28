package Ness.Backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Profile {
    @Id
    @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

    private String pictureUrl;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
