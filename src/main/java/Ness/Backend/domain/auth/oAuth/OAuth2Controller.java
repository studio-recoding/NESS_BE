package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;
    @PostMapping("/login/oauth/{registration}")
    public CommonResponse<?> socialLogin(@RequestParam String code, @PathVariable String registration) {
        String loginMessage = oAuth2Service.socialLogin(code, registration);
        return CommonResponse.postResponse(HttpStatus.OK.value(), loginMessage);
    }
}