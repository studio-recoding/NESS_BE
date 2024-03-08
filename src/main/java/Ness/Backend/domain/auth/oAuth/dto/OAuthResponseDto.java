package Ness.Backend.domain.auth.oAuth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthResponseDto {
    @Schema(description = "구글 이메일 주소", example = "1234@gmail.com")
    private String email;

    @Schema(description = "구글 유저 ID", example = "000000000000000000000")
    private String id;

    @Schema(description = "구글 프로필 사진 URL", example = "https://lh3.googleusercontent.com/...")
    private String picture;
}
