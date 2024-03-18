package Ness.Backend.domain.profile;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.dto.ProfileResponseDto;
import Ness.Backend.domain.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    public Long updateNickname(Long id, String nickname) {
        //JPA 변경 감지 사용
        Profile profile = profileRepository.findProfileByMember_Id(id);
        profile.updateNickname(nickname);

        //결과로 profile의 ID 반환
        return profile.getId();
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long id) {
        Profile profile = profileRepository.findProfileByMember_Id(id);
        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .id(profile.getId())
                .pictureUrl(profile.getPictureUrl())
                .nickname(profile.getNickname())
                .name(profile.getName())
                .build();

        return profileResponseDto;
    }
}
