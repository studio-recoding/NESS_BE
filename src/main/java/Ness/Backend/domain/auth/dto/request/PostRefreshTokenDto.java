package Ness.Backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostRefreshTokenDto {
    @NotNull
    private String jwtRefreshToken;
}
