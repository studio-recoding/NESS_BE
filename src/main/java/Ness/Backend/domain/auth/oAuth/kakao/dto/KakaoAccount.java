package Ness.Backend.domain.auth.oAuth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class KakaoAccount {
    @JsonProperty("profile")
    private KakaoProfile kakaoProfile;
    @JsonProperty("email")
    private String email;
}
