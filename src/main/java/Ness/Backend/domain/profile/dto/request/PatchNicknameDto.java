package Ness.Backend.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatchNicknameDto {
    @Schema(description = "업데이트할 사용자 닉네임", example = "홍길동")
    private String nickname;
}
