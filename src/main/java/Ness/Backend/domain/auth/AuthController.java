package Ness.Backend.domain.auth;

import Ness.Backend.domain.auth.dto.request.PostRefreshTokenDto;
import Ness.Backend.domain.auth.dto.response.GetJwtTokenDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "로그아웃 요청 API 입니다.")
    public ResponseEntity<?> logout(@AuthUser Member member, @RequestBody PostRefreshTokenDto postRefreshTokenDto) {
        authService.logout(member, postRefreshTokenDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/reIssuance")
    @Operation(summary = "JWT access 토큰 재발급 요청", description = "JWT access 토큰 재발급 요청 API 입니다.")
    public GetJwtTokenDto reIssuance(@AuthUser Member member, @RequestBody PostRefreshTokenDto postRefreshTokenDto) {
        return authService.reIssuance(member, postRefreshTokenDto);
    }
}
