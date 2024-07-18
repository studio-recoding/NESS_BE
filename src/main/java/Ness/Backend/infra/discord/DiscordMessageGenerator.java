package Ness.Backend.infra.discord;

import Ness.Backend.global.time.GlobalTime;
import Ness.Backend.infra.discord.dto.DiscordEmbedDto;
import Ness.Backend.infra.discord.dto.DiscordMessageDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordMessageGenerator {
    private final GlobalTime globalTime;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /* 에러 메세지 생성 */
    public DiscordMessageDto createErrorMessage(Exception exception, HttpServletRequest httpServletRequest) {
        return DiscordMessageDto.builder()
            .content("## 🚨 서버 에러 발생 🚨")
            .embeds(List.of(DiscordEmbedDto.builder()
                            .title("ℹ️ 에러 정보")
                            .description("### 🕖 에러 발생 시간\n"
                                    + globalTime.getToday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초(서울 시간)"))
                                    + "\n"
                                    + "### 🔗 요청 엔드포인트\n"
                                    + httpServletRequest.getRequestURI()
                                    + "\n"
                                    + "### 🧐 요청 클라이언트 IP\n"
                                    + getRemoteIp(httpServletRequest)
                                    + "\n"
                                    + "### 🖥️ 에러 발생 서버\n"
                                    + activeProfile
                                    + "\n"
                                    + "### 📜 에러 로그\n"
                                    + "```\n"
                                    + getStackTrace(exception).substring(0, 1000)
                                    + "\n```"
                            ).build()
                    )
            ).build();
    }

    /* 콘솔의 에러 보여주기 */
    private String getStackTrace(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /* 클라이언트 요청 IP 알아내기 */
    private String getRemoteIp(HttpServletRequest httpServletRequest){
        return httpServletRequest.getRemoteAddr();
    }

    /* 인포 메세지 생성 */
    public DiscordMessageDto createInfoMessage(Long memberId, String name) {
        return DiscordMessageDto.builder()
                .content("##🚀 새로운 유저 가입 🚀")
                .embeds(List.of(DiscordEmbedDto.builder()
                                .title("ℹ️ 유저 정보")
                                .description("### 🕖 유저 가입 시간\n"
                                        + globalTime.getToday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초(서울 시간)"))
                                        + "\n"
                                        + "### 🖥️ 유저 정보\n"
                                        + memberId + "번 DB 아이디를 가지신 " + name + "님이 방금 회원가입하셨어요!"
                                        + "\n"
                                ).build()
                        )
                ).build();
    }
}
