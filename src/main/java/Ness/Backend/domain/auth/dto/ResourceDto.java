package Ness.Backend.domain.auth.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ResourceDto {
    private String id;

    private String email;

    private String picture;

    private String nickname;

    private String name;

    @Builder
    public ResourceDto(String id, String email, String picture, String nickname, String name){
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
        this.name = name;
    }
}

