package Ness.Backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    /* 사용자의 이메일과 비밀번호로 로그인 */
    @Schema(description = "사용자의 이메일 주소", example = "1234@email.com")
    private String email;

    @Schema(description = "사용자의 비밀번호", example = "abc123!#")
    private String password;
}
