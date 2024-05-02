package Ness.Backend.domain.profile.email;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.Profile;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

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
            sendEmailNotice(email);
        }
    }

    @Async
    public void sendEmailNotice(String email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("End of Today with NESS"); // 메일 제목
            mimeMessageHelper.setText(setContext(getTodayDate()), true);
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    public String getTodayDate(){
        ZonedDateTime todayDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).atZone(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        return todayDate.format(formatter);
    }

    //thymeleaf를 통한 html 적용
    public String setContext(String date) {
        Context context = new Context();
        context.setVariable("date", date);
        context.setVariable("image", "https://ness-static-s3.s3.ap-northeast-2.amazonaws.com/email_sample.png");
        return templateEngine.process("end-of-today", context);
    }
}
