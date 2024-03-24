package Ness.Backend.domain.auth;

import Ness.Backend.domain.auth.dto.LoginRequestDto;
import Ness.Backend.domain.auth.dto.RegisterRequestDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import Ness.Backend.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "회원 가입 및 로그인 인증 API.")
@RequestMapping
public class AuthController {
    private final AuthService authService;

    /* 스프링 시큐리티는 /login 경로의 요청을 받아 인증 과정을 처리 */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "사용자의 이메일과 비밀번호로 로그인하는 API 입니다.")
    public CommonResponse<?> userLogin(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "로그아웃 요청 API 입니다.")
    public void logout(@AuthUser Member member) {
        //추후 추가 예정
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "사용자의 이메일과 비밀번호로 회원가입하는 API 입니다.")
    public CommonResponse<?> userRegister(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.signUp(registerRequestDto);
    }

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원탈퇴 요청", description = "회원탈퇴 요청 API 입니다.")
    public void withdrawal(@AuthUser Member member) {
        //추후 추가 예정
    }
}
