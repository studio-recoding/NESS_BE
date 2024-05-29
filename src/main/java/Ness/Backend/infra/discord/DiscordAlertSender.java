package Ness.Backend.infra.discord;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordAlertSender {
    private final DiscordClientApi discordClientApi;
    private final DiscordMessageGenerator discordMessageGenerator;

    public void sendDiscordAlarm(Exception exception, HttpServletRequest httpServletRequest) {
        discordClientApi.sendAlarm(discordMessageGenerator.createMessage(exception, httpServletRequest));
    }
}
