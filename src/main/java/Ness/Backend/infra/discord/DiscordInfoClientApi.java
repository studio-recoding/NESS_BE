package Ness.Backend.infra.discord;

import Ness.Backend.infra.discord.dto.DiscordMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "discordInfoClientApi",
        url = "${discord.webhook.info}")
public interface DiscordInfoClientApi {
    @PostMapping()
    void sendAlarm(@RequestBody DiscordMessageDto discordMessageDto);
}