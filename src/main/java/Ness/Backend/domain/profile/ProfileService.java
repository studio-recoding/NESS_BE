package Ness.Backend.domain.profile;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.dto.request.PutProfileDto;
import Ness.Backend.domain.profile.dto.response.GetProfileDto;
import Ness.Backend.domain.profile.entity.PersonaType;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.infra.s3.S3Service;
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
    private final S3Service s3Service;

    public Long updateProfile(Long memberId, PutProfileDto putProfileDto){
        //JPA 변경 감지 사용
        Profile profile = profileRepository.findProfileByMember_Id(memberId);
        profile.updateProfile(putProfileDto.getNickname(), putProfileDto.getKey());

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
    public GetProfileDto getProfile(Long memberId, String email) {
        Profile profile = profileRepository.findProfileByMember_Id(memberId);
        GetProfileDto getProfileDto = GetProfileDto.builder()
                .id(profile.getId())
                .pictureUrl(createPictureUrl(memberId, profile))
                .nickname(profile.getNickname())
                .name(profile.getName())
                .isEmailActive(profile.getIsEmailActive())
                .email(email)
                .persona(profile.getPersonaType())
                .build();

        return getProfileDto;
    }

    /* S3에 사진이 있다면 presigned URL로 보내주고, 없다면 OAuth 로그인으로 생성된 프로필 사진 보내주기 */
    private String createPictureUrl(Long memberId, Profile profile){
        if(profile.getPictureKey() == null){
            return profile.getPictureUrl();
        }
        return s3Service.getGetS3Url(memberId,
                        profile.getPictureKey()).getPreSignedUrl();
    }
}
