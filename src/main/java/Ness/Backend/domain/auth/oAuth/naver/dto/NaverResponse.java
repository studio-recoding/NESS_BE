package Ness.Backend.domain.auth.oAuth.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class NaverResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("profile_image")
    private String picture;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("name")
    private String name;
}
