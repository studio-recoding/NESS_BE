package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;

    @PostMapping("/login/oauth/{registration}")
    @Operation(summary = "OAuth 로그인 요청", description = "구글 계정으로 로그인하는 API 입니다.")
    public ResponseEntity<?> socialLogin(@RequestParam String code, @PathVariable String registration) {
        return new ResponseEntity<>(oAuth2Service.socialLogin(code, registration), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/logout/oauth/{registration}")
    @Operation(summary = "OAuth 로그아웃 요청", description = "구글 계정 로그아웃 요청 API 입니다.")
    public void logout(@AuthUser Member member) {
        oAuth2Service.logout(member);
    }

    @DeleteMapping("/withdrawal/oauth/{registration}")
    @Operation(summary = "OAuth 회원탈퇴 요청", description = "구글 계정 회원탈퇴 요청 API 입니다.")
    public void withdrawal(@AuthUser Member member) {
        oAuth2Service.withdrawal(member);
    }

    @PostMapping("/reIssuance")
    @Operation(summary = "OAuth JWT access 토큰 재발급 요청", description = "JWT access 토큰 재발급 요청 API 입니다.")
    public void reIssuance(@AuthUser Member member) {
        //추후 구현 예정
        //return oAuth2Service.reIssuance(member);
    }
}