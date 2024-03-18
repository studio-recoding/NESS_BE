package Ness.Backend.domain.member;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void deleteMember(Member member) {
        profileRepository.delete(member.getProfile());
        /* 소프트 삭제 */
        member.updateIsDeleted();
        memberRepository.save(member);
    }

    public Member createMember(String email, String password, String picture, String nickname, String name) {
        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password)) //비밀번호는 해싱해서 DB에 저장
                .build();

        Profile profile = Profile.builder()
                .pictureUrl(picture)
                .nickname(nickname)
                .name(name)
                .member(member)
                .build();

        profileRepository.save(profile);
        return memberRepository.save(member);
    }
}
