package Ness.Backend.domain.profile;

import Ness.Backend.domain.profile.dto.response.GetProfileDto;
import Ness.Backend.domain.profile.entity.PersonaType;
import Ness.Backend.domain.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    public Long updateNickname(Long memberId, String nickname) {
        //JPA 변경 감지 사용
        Profile profile = profileRepository.findProfileByMember_Id(memberId);
        profile.updateNickname(nickname);

        //결과로 profile의 ID 반환
        return profile.getId();
    }

    public Long updatePersona(Long memberId, PersonaType personaType) {
        //JPA 변경 감지 사용
        Profile profile = profileRepository.findProfileByMember_Id(memberId);
        profile.updatePersona(personaType);

        //결과로 profile의 ID 반환
        return profile.getId();
    }

    @Transactional(readOnly = true)
    public GetProfileDto getProfile(Long id) {
        Profile profile = profileRepository.findProfileByMember_Id(id);
        GetProfileDto getProfileDto = GetProfileDto.builder()
                .id(profile.getId())
                .pictureUrl(profile.getPictureUrl())
                .nickname(profile.getNickname())
                .name(profile.getName())
                .isEmailActive(profile.getIsEmailActive())
                .persona(profile.getPersonaType())
                .build();

        return getProfileDto;
    }
}
