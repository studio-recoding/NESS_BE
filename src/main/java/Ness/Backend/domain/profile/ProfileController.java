package Ness.Backend.domain.profile;


import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.dto.request.PatchNicknameDto;
import Ness.Backend.domain.profile.dto.response.GetProfileDto;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회 API", description = "사용자의 ID로 프로필을 조회하는 API 입니다.")
    public GetProfileDto userLogin(@AuthUser Member member) {
        return profileService.getProfile(member.getId());
    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 닉네임 변경 API", description = "사용자의 ID로 프로필 닉네임을 변경하는 API 입니다.")
    public ResponseEntity<Long> userLogin(@AuthUser Member member, PatchNicknameDto patchNicknameDto) {
        Long profileId = profileService.updateNickname(member.getId(), patchNicknameDto.getNickname());
        return new ResponseEntity<>(profileId, HttpStatusCode.valueOf(200));
    }
}
