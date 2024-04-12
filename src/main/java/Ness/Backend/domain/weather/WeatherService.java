package Ness.Backend.domain.weather;

import Ness.Backend.domain.weather.dto.response.GetPublicDataWeatherDto;
import Ness.Backend.global.publicData.PublicDataWeatherApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    private final PublicDataWeatherApi publicDataWeatherApi;

    @Value("${api-key.public-weather}")
    private String publicWeatherApiKey;

    public GetPublicDataWeatherDto getWeather(){
        return getPublicDataWeather();
    }

    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public int findBaseTime(){
        /* 가능한 Base_time 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300
            02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
         */
        return 0;
    }

    //단기 예보 조회 호출
    public GetPublicDataWeatherDto getPublicDataWeather(){
        log.info(getToday().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        log.info(getToday().format(DateTimeFormatter.ofPattern("Hm")));
        return publicDataWeatherApi.getPublicDataWeather(
                "application/json",
                publicWeatherApiKey, 2, 2, "JSON",
                getToday().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                "0630",
                //getToday().format(DateTimeFormatter.ofPattern("Hm")),
                126.999641666666, 37.5610027777777); //테스트용 값
    }
}
