package Ness.Backend.domain.profile.email;

import Ness.Backend.domain.profile.email.dto.request.PostFastApiUserEmailDto;
import Ness.Backend.domain.profile.email.dto.response.PostFastApiAiEmailDto;
import Ness.Backend.global.fastApi.FastApiEmailApi;
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
    private final FastApiEmailApi fastApiEmailApi;
    @Async
    public void sendEmailNotice(Long memberId, String email){
        log.info("Trying to send Email to " + email);
        try {
            PostFastApiAiEmailDto aiDto = postTodayAiAnalysis(memberId, getToday());
            String image = aiDto.getImage();
            String text = aiDto.getText().replace("<br>", "\n");

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("End of Today with NESS"); // 메일 제목
            mimeMessageHelper.setText(setContext(getTodayDate(), image, text), true);
            javaMailSender.send(mimeMessage);
            log.info("Succeeded to send Email to " + email);
        } catch (Exception e) {
            log.info("Failed to send Email to " + email + ", Error log: ", e);
            throw new RuntimeException(e);
        }
    }

    public PostFastApiAiEmailDto postTodayAiAnalysis(Long id, ZonedDateTime today){
        PostFastApiUserEmailDto userDto = PostFastApiUserEmailDto.builder()
                .member_id(id.intValue())
                .user_persona("")
                .schedule_datetime_start(today)
                .schedule_datetime_end(today)
                .build();

        //Fast API에 전송하고 값 받아오기
        return fastApiEmailApi.creatFastApiEmail(userDto);
    }

    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public String getTodayDate(){
        ZonedDateTime todayDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).atZone(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        return todayDate.format(formatter);
    }

    //thymeleaf를 통한 html 적용
    public String setContext(String date, String image, String text) {
        Context context = new Context();
        context.setVariable("date", date);
        context.setVariable("image", image);
        context.setVariable("text", text);
        return templateEngine.process("end-of-today", context);
    }
}
