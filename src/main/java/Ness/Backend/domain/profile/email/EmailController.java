package Ness.Backend.domain.profile.email;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PatchMapping("")
    @Operation(summary = "이메일 알림 기능 활성화/비활성화 API", description = "이메일 기능 활성화/비활성화를 맡는 API입니다.")
    public ResponseEntity<?> sendOverview(@AuthUser Member member, @RequestParam Boolean isActive){
        emailService.setEmail(member.getId(), isActive);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/test")
    @Operation(summary = "이메일 기능 테스트 API", description = "사용자가 어떤 이메일이 오는지 궁금할 때 테스트할 수 있는 API 입ㄴ디ㅏ.")
    public ResponseEntity<?> sendOverview(@AuthUser Member member){
        emailService.sendEmailTest(member.getId(), member.getEmail());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
