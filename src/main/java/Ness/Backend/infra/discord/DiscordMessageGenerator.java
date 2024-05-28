package Ness.Backend.infra.discord;

import Ness.Backend.infra.discord.dto.DiscordEmbedDto;
import Ness.Backend.infra.discord.dto.DiscordMessageDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordMessageGenerator {
    private final DiscordClientApi discordClientApi;
    public void sendDiscordAlarm(Exception exception, HttpServletRequest httpServletRequest) {
        discordClientApi.sendAlarm(createMessage(exception, httpServletRequest));
    }

    /* 메세지 생성 */
    public DiscordMessageDto createMessage(Exception exception, HttpServletRequest httpServletRequest) {
        return DiscordMessageDto.builder()
            .content("# 🚨서버 에러 발생🚨")
            .embeds(List.of(DiscordEmbedDto.builder()
                            .title("ℹ️에러 정보")
                            .description("### 🕖발생 시간\n"
                                    + LocalDateTime.now()
                                    + "\n"
                                    + "### 🔗요청 URL\n"
                                    + httpServletRequest.getRequestURI()
                                    + "\n"
                                    + "### 📄Stack Trace\n"
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
