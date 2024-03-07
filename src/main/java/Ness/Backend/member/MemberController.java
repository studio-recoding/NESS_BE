package Ness.Backend.member;

import Ness.Backend.auth.security.AuthDetails;
import Ness.Backend.global.auth.AuthUser;
import Ness.Backend.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증 테스트 API", description = "JWT 토큰 인증 테스트용 API.")
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/user/info")
    @Operation(summary = "맴버 엔티티 반환 API", description = "JWT 토큰을 바탕으로 맴버 엔티티를 반환하는 테스트용 API 입니다.")
    public ResponseEntity<Member> getMyPageData(Authentication authentication) {
        // 인증된 사용자의 정보를 가져오기
        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        String email = authDetails.getUsername(); // 여기서는 username에 email을 담았습니다.

        // 사용자 ID를 이용하여 데이터베이스에서 사용자 정보 조회
        Member member = memberRepository.findMemberByEmail(email);
        return new ResponseEntity<>(member, HttpStatusCode.valueOf(200));
    }

    /* Member 그대로 반환 불가 -> Dto에 넣어서 */
    @GetMapping("/user/authuser")
    @Operation(summary = "맴버 엔티티 반환 API", description = "JWT 토큰을 바탕으로 맴버 엔티티를 반환하는 테스트용 API 입니다.")
    public ResponseEntity<MemberDto> getAuthUser(@AuthUser Member member) {
        System.out.println(member.getEmail());
        //Dto로 반환하지 않을 경우 에러 발생
        MemberDto memberDto = MemberDto.builder()
                .email(member.getEmail())
                .id(member.getId())
                .build();
        return new ResponseEntity<>(memberDto, HttpStatusCode.valueOf(200));
    }
}
