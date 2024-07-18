package Ness.Backend.infra.discord;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.registry.selector.spi.DialectSelector;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordAlertSender {
    private final DiscordErrorClientApi discordErrorClientApi;
    private final DiscordInfoClientApi discordInfoClientApi;
    private final DiscordMessageGenerator discordMessageGenerator;

    public void sendDiscordErrorAlarm(Exception exception, HttpServletRequest httpServletRequest) {
        discordErrorClientApi.sendAlarm(discordMessageGenerator.createErrorMessage(exception, httpServletRequest));
    }

    public void sendDiscordInfoAlarm(Long memberId, String name) {
        discordInfoClientApi.sendAlarm(discordMessageGenerator.createInfoMessage(memberId, name));
    }
}
