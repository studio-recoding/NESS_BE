package Ness.Backend.domain.email;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/todo")
    @Operation(summary = "오늘의 todo 제공 API", description = "오늘의 todo를 이메일로 제공해주는 API 입니다.")
    public ResponseEntity<?> sendOverview(@AuthUser Member member){
        emailService.sendEmailNotice(member.getEmail());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
