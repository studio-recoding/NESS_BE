package Ness.Backend.domain.auth.inmemory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
//@RedisHash(value = "refreshToken", timeToLive = 60*60*24*14)
@RedisHash(value = "refreshToken", timeToLive = 5)
public class RefreshToken { /* Redis에 저장해서 RefreshToken이 유효한지 검증 */
    @Id
    @Indexed
    private String jwtRefreshToken;

    // 맴버 이메일로 설정
    private String authKey;

    //리프레시 토큰의 생명 주기(14일)
    @TimeToLive
    private Long ttl;

    @Builder
    public RefreshToken(String jwtRefreshToken, String authKey) {
        this.jwtRefreshToken = jwtRefreshToken;
        this.authKey = authKey;
        //this.ttl = 1000L * 60 * 60 * 24 * 14;
        this.ttl = 1000L * 5;
    }
}