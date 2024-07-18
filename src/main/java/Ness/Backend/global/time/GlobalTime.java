package Ness.Backend.global.time;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class GlobalTime {
    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public ZonedDateTime getUpcomingOneHourTime(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(1);
    }

    public ZonedDateTime getUpcomingTwoHourTime(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(2);
    }

    public ZonedDateTime createdZonedDate(){
        return LocalDateTime
                .now(ZoneId.of("Asia/Seoul"))
                .atZone(ZoneId.of("Asia/Seoul"));
    }
}