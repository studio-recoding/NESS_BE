package Ness.Backend.domain.weather;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/dev")
    @Operation(summary = "날씨 정보 요청", description = "기상청에서 초단기예보 날씨 정보를 가져오는 API입니다.")
    public ResponseEntity<?> getWeather() {
        return new ResponseEntity<>(weatherService.getWeather(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("")
    @Operation(summary = "날씨 정보 요청", description = "기상청에서 초단기예보 날씨 정보를 가져오는 API입니다.")
    public ResponseEntity<?> getWeather(@AuthUser Member member) {
        return new ResponseEntity<>(weatherService.getWeather(), HttpStatusCode.valueOf(200));
    }
}
