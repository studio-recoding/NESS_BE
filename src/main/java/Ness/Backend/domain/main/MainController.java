package Ness.Backend.domain.main;

import Ness.Backend.domain.main.dto.response.GetMainDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main")
public class MainController {
    private final MainService mainService;

    @GetMapping("")
    public ResponseEntity<?> getMain(@AuthUser Member member){
        GetMainDto getMainDto = mainService.getMain(member.getId());
        return new ResponseEntity<>(getMainDto, HttpStatusCode.valueOf(200));
    }
}
