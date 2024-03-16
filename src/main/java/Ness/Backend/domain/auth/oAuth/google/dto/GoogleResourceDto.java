package Ness.Backend.domain.auth.oAuth.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class GoogleResourceDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("name")
    private String nickname;

    @Builder
    public GoogleResourceDto(String id, String email, String picture, String nickname){
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
    }
}
