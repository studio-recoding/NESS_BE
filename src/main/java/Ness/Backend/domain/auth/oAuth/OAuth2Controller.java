package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import Ness.Backend.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;
    @PostMapping("/login/oauth/{registration}")
    @Operation(summary = "로그인 API", description = "구글 계정으로 로그인하는 API 입니다.")
    public CommonResponse<?> socialLogin(@RequestParam String code, @PathVariable String registration) {
        String loginMessage = oAuth2Service.socialLogin(code, registration);
        return CommonResponse.postResponse(HttpStatus.OK.value(), loginMessage);
    }

    @PostMapping("/logout/oauth/{registration}")
    @Operation(summary = "로그아웃 요청", description = "구글 계정 로그아웃 요청 API 입니다.")
    public void logout(@AuthUser Member member) {
        oAuth2Service.logout(member);
    }

    @DeleteMapping("/withdrawal/oauth/{registration}")
    @Operation(summary = "회원탈퇴 요청", description = "구글 계정 회원탈퇴 요청 API 입니다.")
    public void withdrawal(@AuthUser Member member) {
        oAuth2Service.withdrawal(member);
    }

    @PostMapping("/reIssuance")
    @Operation(summary = "JWT access 토큰 재발급 요청", description = "JWT access 토큰 재발급 요청 API 입니다.")
    public void reIssuance(@AuthUser Member member) {
        //추후 구현 예정
        //return oAuth2Service.reIssuance(member);
    }
}