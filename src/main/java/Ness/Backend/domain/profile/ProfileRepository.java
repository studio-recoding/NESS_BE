package Ness.Backend.domain.profile;

import Ness.Backend.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // 특정 맴버 ID로 프로필 반환
    Profile findProfileByMember_Id(Long memberId);
}
