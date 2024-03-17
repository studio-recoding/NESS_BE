package Ness.Backend.domain.auth.oAuth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class KakaoProfile {
    @JsonProperty("profile_image_url")
    private String picture;

    @JsonProperty("nickname")
    private String nickname;
}
