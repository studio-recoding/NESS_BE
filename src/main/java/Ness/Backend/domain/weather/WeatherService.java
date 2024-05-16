package Ness.Backend.domain.weather;

import Ness.Backend.domain.weather.dto.response.PublicDataWeather;
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

    public PublicDataWeather getWeather(){
        return getPublicDataWeather();
    }

    public ZonedDateTime getToday(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    /*
    * Map<String, Double> categoryAverages = items.stream()
                .collect(Collectors.groupingBy(
                        WeatherItemDto::getCategory,
                        Collectors.averagingDouble(item -> Double.parseDouble(item.getFcstValue()))
                ));
    * */

    public PublicDataWeather getPublicDataWeather(){
        return publicDataWeatherApi.getPublicDataWeather(
                "application/json",
                publicWeatherApiKey, 60, 1, "JSON",
                getToday().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                getToday().format(DateTimeFormatter.ofPattern("Hm")),
                59, 127); // 우선은 서울 서대문구로 하드코딩
    }
}
