package Ness.Backend.domain.profile.email;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@Transactional(readOnly = true)
public class EmailService {
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final AsyncEmailService asyncEmailService;

    @Transactional
    public void setEmail(Long memberId, Boolean isActive){
        Profile profile = profileRepository.findProfileByMember_Id(memberId);
        profile.updateMailActive(isActive); // 이메일 활성화 여부 변경
    }

    // 매일 오전 자정에 스케쥴링
    @Scheduled(cron = "0 0 12 * * *")
    public void scheduleEmailCron(){
        log.info("스케쥴링 활성화");
        List<Member> activeMembers = memberRepository.findMembersByProfileIsEmailActive(true);

        for (Member member : activeMembers) {
            String email = member.getEmail();
            asyncEmailService.sendEmailNotice(email);
        }
    }

    public void sendEmailTest(String email){
        asyncEmailService.sendEmailNotice(email);
    }
}
