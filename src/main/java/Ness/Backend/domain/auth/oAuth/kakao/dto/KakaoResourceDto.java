package Ness.Backend.domain.auth.oAuth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class KakaoResourceDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("profile_image_url")
    private String picture;

    @JsonProperty("nickname")
    private String nickname;

    @Builder
    public KakaoResourceDto(String id, String email, String picture, String nickname){
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
    }
}
