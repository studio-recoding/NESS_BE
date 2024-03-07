package Ness.Backend.auth.oAuth;

import Ness.Backend.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    @GetMapping("/login/oauth/{registration}")
    public CommonResponse<?> socialLogin(@RequestParam String code, @PathVariable String registration) {
        String loginMessage = oAuthService.socialLogin(code, registration);
        return CommonResponse.postResponse(HttpStatus.OK.value(), loginMessage);
    }

    @PostMapping("/signup/oauth/{registration}")
    public CommonResponse<?> socialSignUp(@RequestParam String code, @PathVariable String registration) {
        String registerMessage = oAuthService.socialLogin(code, registration);
        return CommonResponse.postResponse(HttpStatus.CREATED.value(), registerMessage);
    }
}