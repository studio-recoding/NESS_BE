package Ness.Backend.auth.oAuth;

import Ness.Backend.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;
    @GetMapping("/login/oauth/{registration}")
    public CommonResponse<?> socialLogin(@RequestParam String code, @PathVariable String registration) {
        String loginMessage = oAuth2Service.socialLogin(code, registration);
        return CommonResponse.postResponse(HttpStatus.OK.value(), loginMessage);
    }

    @PostMapping("/signup/oauth/{registration}")
    public CommonResponse<?> socialSignUp(@RequestParam String code, @PathVariable String registration) {
        String registerMessage = oAuth2Service.socialSignUp(code, registration);
        return CommonResponse.postResponse(HttpStatus.CREATED.value(), registerMessage);
    }
}