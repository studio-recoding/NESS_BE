package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.auth.jwt.entity.JwtToken;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import Ness.Backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;

    @GetMapping("/oauth/google/success")
    @Operation(summary = "개발용 회원가입으로, 클라이언트는 모르는 테스트 용입니다.")
    public ApiResponse<JwtToken> devSocialLogin(@RequestParam(value = "jwtAccessToken") String jwtAccessToken, @RequestParam(value = "jwtRefreshToken") String jwtRefreshToken) {
        JwtToken jwtToken = JwtToken.builder()
                .jwtAccessToken(jwtAccessToken)
                .jwtRefreshToken(jwtRefreshToken)
                .build();
        return ApiResponse.getResponse(HttpStatus.OK.value(), "테스트용 ", jwtToken);
    }
//
//    @PostMapping("/login/oauth/{registration}")
//    @Operation(summary = "OAuth 로그인 요청", description = "구글 계정으로 로그인하는 API 입니다.")
//    public ResponseEntity<?> socialLogin(@RequestParam String code, @PathVariable String registration) {
//        return new ResponseEntity<>(oAuth2Service.socialLogin(code, registration), HttpStatusCode.valueOf(200));
//    }
//
//
//
//    @PostMapping("/logout/oauth/{registration}")
//    @Operation(summary = "OAuth 로그아웃 요청", description = "구글 계정 로그아웃 요청 API 입니다.")
//    public void logout(@AuthUser Member member) {
//        oAuth2Service.logout(member);
//    }
//
//    @DeleteMapping("/withdrawal/oauth/{registration}")
//    @Operation(summary = "OAuth 회원탈퇴 요청", description = "구글 계정 회원탈퇴 요청 API 입니다.")
//    public void withdrawal(@AuthUser Member member) {
//        oAuth2Service.withdrawal(member);
//    }
//
//    @PostMapping("/reIssuance")
//    @Operation(summary = "OAuth JWT access 토큰 재발급 요청", description = "JWT access 토큰 재발급 요청 API 입니다.")
//    public void reIssuance(@AuthUser Member member) {
//        //추후 구현 예정
//        //return oAuth2Service.reIssuance(member);
//    }
}