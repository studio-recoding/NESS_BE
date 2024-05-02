package Ness.Backend.domain.profile.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class AsyncEmailService {
    /* Async는 프록시 기반 동작이므로, self-invocation(자가 호출) 불가->다른 클래스로 분리*/
    // TODO: setQueueCapacity로 비동기로 처리 가능한 이메일 제한 필요
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @Async
    public void sendEmailNotice(String email){
        log.info(email);
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
