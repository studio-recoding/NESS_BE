package Ness.Backend.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PutProfileDto {
    @Schema(description = "업데이트할 사용자 닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "업데이트할 사용자 프로필 키 경로", example = "untitle.png")
    private String key;
}
