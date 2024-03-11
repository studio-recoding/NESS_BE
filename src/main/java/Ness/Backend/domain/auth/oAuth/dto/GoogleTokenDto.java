package Ness.Backend.domain.auth.oAuth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenDto {
    @NotNull(message = "accessToken may not be null")
    @JsonProperty("access_token")
    private String accessToken;
}
