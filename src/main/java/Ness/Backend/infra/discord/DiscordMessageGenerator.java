package Ness.Backend.infra.discord;

import Ness.Backend.infra.discord.dto.DiscordEmbedDto;
import Ness.Backend.infra.discord.dto.DiscordMessageDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordMessageGenerator {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /* 메세지 생성 */
    public DiscordMessageDto createMessage(Exception exception, HttpServletRequest httpServletRequest) {
        return DiscordMessageDto.builder()
            .content("## 🚨 서버 에러 발생 🚨")
            .embeds(List.of(DiscordEmbedDto.builder()
                            .title("ℹ️ 에러 정보")
                            .description("### 🕖 에러 발생 시간\n"
                                    + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초(서울 시간)"))
                                    + "\n"
                                    + "### 🔗 요청 엔드포인트\n"
                                    + httpServletRequest.getRequestURI()
                                    + "\n"
                                    + "### 🖥️ 에러 발생 서버\n"
                                    + activeProfile
                                    + "\n"
                                    + "### 📜 에러 로그\n"
                                    + "```\n"
                                    + getStackTrace(exception).substring(0, 1000)
                                    + "\n```")
                            .build()
                            )
            ).build();
    }

    /* 콘솔의 에러 보여주기 */
    private String getStackTrace(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
