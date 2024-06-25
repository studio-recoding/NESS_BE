package Ness.Backend.domain.profile;


import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.dto.request.PatchPersonaDto;
import Ness.Backend.domain.profile.dto.request.PutProfileDto;
import Ness.Backend.domain.profile.dto.response.GetOnBoardingDto;
import Ness.Backend.domain.profile.dto.response.GetProfileDto;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("")
    @Operation(summary = "프로필 조회 API", description = "사용자의 ID로 프로필을 조회하는 API 입니다.")
    public GetProfileDto userLogin(@AuthUser Member member) {
        return profileService.getProfile(member.getId(), member.getEmail());
    }

    @PutMapping("")
    @Operation(summary = "프로필 변경 API", description = "프로필 닉네임과 사진을 모두 변경하는 API 입니다.")
    public ResponseEntity<Long> patchProfile(@AuthUser Member member, @RequestBody PutProfileDto putProfileDto) {
        Long profileId = profileService.updateProfile(member.getId(), putProfileDto);
        return new ResponseEntity<>(profileId, HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/persona")
    @Operation(summary = "페르소나 변경 API", description = "페르소나를 변경하는 API 입니다.")
    public ResponseEntity<Long> patchPersona(@AuthUser Member member, @RequestBody PatchPersonaDto patchPersonaDto) {
        Long profileId = profileService.updatePersona(member.getId(), patchPersonaDto.getPersona());
        return new ResponseEntity<>(profileId, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/onboarding")
    @Operation(summary = "온보딩 여부 확인 API", description = "온보딩 여부를 알 수 있는 API 입니다.")
    public ResponseEntity<GetOnBoardingDto> getOnBoarding(@AuthUser Member member) {
        GetOnBoardingDto getOnBoardingDto = profileService.getOnBoarding(member.getId());
        return new ResponseEntity<>(getOnBoardingDto, HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/onboarding")
    @Operation(summary = "온보딩 여부 수정 API", description = "온보딩 여부를 수정할 수 있는 API 입니다.")
    public ResponseEntity<?> updateOnBoarding(@AuthUser Member member, @RequestParam Boolean isOnBoarded) {
        profileService.updateOnBoarding(member.getId(), isOnBoarded);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
