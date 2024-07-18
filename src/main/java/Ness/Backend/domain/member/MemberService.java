package Ness.Backend.domain.member;

import Ness.Backend.domain.category.CategoryRepository;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.PersonaType;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.domain.schedule.ScheduleService;
import Ness.Backend.domain.schedule.dto.request.PostScheduleDto;
import Ness.Backend.global.time.GlobalTime;
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
    private final ScheduleService scheduleService;
    private final GlobalTime globalTime;

    public void deleteMember(Member member) {
        profileRepository.delete(member.getProfile());
        /* 소프트 삭제 */
        member.updateIsDeleted();
        memberRepository.save(member);
    }

    public void createMember(String email, String password, String picture, String nickname,
                             String name, Boolean isEmailActive, Boolean isOnBoarded) {
        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password)) //비밀번호는 해싱해서 DB에 저장
                .build();

        memberRepository.save(member);

        Profile profile = Profile.builder()
                .pictureUrl(picture)
                .nickname(nickname)
                .name(name)
                .member(member)
                .isEmailActive(isEmailActive) // 디폴트로 true, 나중에 개인 페이지에서 변경 가능
                .personaType(PersonaType.NESS) //디폴트로 NESS를 저장해줌, 나중에 개인 페이지에서 변경 가능
                .onBoarding(isOnBoarded) // 디폴트로 false, 온보딩 마치면 true 변환
                .build();

        profileRepository.save(profile);

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
                .name("✨여가")
                .color("#FF75C8")
                .build();

        //주황
        Category meetingCategory = Category.builder()
                .member(member)
                .name("\uD83D\uDC65약속")
                .color("#FFB775")
                .build();

        categoryRepository.save(noneCategory);
        categoryRepository.save(studyCategory);
        categoryRepository.save(workoutCategory);
        categoryRepository.save(restCategory);
        categoryRepository.save(meetingCategory);

        // 예시 스케쥴 2개 생성
        PostScheduleDto oneHourLaterSchedule = PostScheduleDto.builder()
                .info("NESS 온보딩 진행하기")
                .location("현재 위치")
                .person("")
                .startTime(globalTime.getUpcomingOneHourTime())
                .endTime(globalTime.getUpcomingOneHourTime().plusMinutes(30))
                .categoryNum(meetingCategory.getId())
                //.chat() //chat 종속성은 없음
                .build();

        PostScheduleDto twoHourLaterSchedule = PostScheduleDto.builder()
                .info("NESS 사용법 공부하기")
                .location("")
                .person("NESS")
                .startTime(globalTime.getUpcomingTwoHourTime())
                .endTime(globalTime.getUpcomingTwoHourTime().plusMinutes(30))
                .categoryNum(studyCategory.getId())
                //.chat() //chat 종속성은 없음
                .build();

        scheduleService.postNewUserSchedule(member.getId(), oneHourLaterSchedule);
        scheduleService.postNewUserSchedule(member.getId(), twoHourLaterSchedule);

        return;
    }
}
