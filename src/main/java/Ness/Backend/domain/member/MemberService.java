package Ness.Backend.domain.member;

import Ness.Backend.domain.category.CategoryRepository;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.PersonaType;
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
    private final CategoryRepository categoryRepository;
    public void deleteMember(Member member) {
        profileRepository.delete(member.getProfile());
        /* 소프트 삭제 */
        member.updateIsDeleted();
        memberRepository.save(member);
    }

    public Member createMember(String email, String password, String picture, String nickname, String name, Boolean isEmailActive) {
        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password)) //비밀번호는 해싱해서 DB에 저장
                .build();

        Profile profile = Profile.builder()
                .pictureUrl(picture)
                .nickname(nickname)
                .name(name)
                .member(member)
                .isEmailActive(isEmailActive)
                .personaType(PersonaType.NESS) //디폴트로 NESS를 저장해줌, 나중에 개인 페이지에서 변경 가능
                .build();

        //디폴트 미분류 카테고리가 있어야 함, 맴버 생성시 자동 만들어주기
        //회색
        Category noneCategory = Category.builder()
                .member(member)
                .name("\uD83C\uDF40미분류")
                .color("#D9D9D9")
                .isDefaultNone(true)
                .build();

        //초록
        Category studyCategory = Category.builder()
                .member(member)
                .name("\uD83D\uDCD6공부")
                .color("#00C09E")
                .build();

        //하늘
        Category workoutCategory = Category.builder()
                .member(member)
                .name("\uD83C\uDFC3\u200D♀\uFE0F운동")
                .color("#759CFF")
                .build();

        //핑크
        Category restCategory = Category.builder()
                .member(member)
                .name("\uD83D\uDEDF여가")
                .color("#FF75C8")
                .build();

        //주황
        Category meetingCategory = Category.builder()
                .member(member)
                .name("\uD83D\uDC65약속")
                .color("#FFB775")
                .build();

        profileRepository.save(profile);
        categoryRepository.save(noneCategory);
        categoryRepository.save(studyCategory);
        categoryRepository.save(workoutCategory);
        categoryRepository.save(restCategory);
        categoryRepository.save(meetingCategory);
        return memberRepository.save(member);
    }
}
