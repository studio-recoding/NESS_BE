package Ness.Backend.infra.discord;

import Ness.Backend.infra.discord.dto.DiscordMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "discordClientApi",
        url = "${discord.webhook.url}")
public interface DiscordClientApi {
    @PostMapping()
    void sendAlarm(@RequestBody DiscordMessageDto discordMessageDto);
}