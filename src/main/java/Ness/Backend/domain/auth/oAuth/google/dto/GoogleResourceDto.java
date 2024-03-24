package Ness.Backend.domain.auth.oAuth.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String name;

    @Builder
    public GoogleResourceDto(String id, String email, String picture, String name){
        this.id = id;
        this.email = email;
        this.picture = picture;
        this.name = name;
    }
}
