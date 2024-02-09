package Ness.Backend.oAuth;

import Ness.Backend.oAuth.dto.GoogleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth", produces = "application/json")
public class OAuthController {
    private final OAuthService oAuthService;
    @GetMapping("/{registration}")
    public ResponseEntity<GoogleResponseDto> googleLogin(@RequestParam String code, @PathVariable String registration) {
        GoogleResponseDto googleResponseDto = oAuthService.socialLogin(code, registration);
        return new ResponseEntity<>(googleResponseDto, HttpStatusCode.valueOf(200));
    }
}