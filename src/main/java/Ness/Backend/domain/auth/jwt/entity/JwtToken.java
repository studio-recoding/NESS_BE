package Ness.Backend.domain.auth.jwt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class JwtToken {
    @JsonProperty("access_token")
    private String jwtAccessToken;

    @JsonProperty("refresh_token")
    private String jwtRefreshToken;

    @Builder
    public JwtToken(String jwtAccessToken, String jwtRefreshToken, Date expRT) {
        this.jwtAccessToken = jwtAccessToken;
        this.jwtRefreshToken = jwtRefreshToken;
    }
}