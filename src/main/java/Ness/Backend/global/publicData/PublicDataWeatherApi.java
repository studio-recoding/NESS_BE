package Ness.Backend.global.publicData;

import Ness.Backend.domain.weather.dto.response.GetPublicDataWeatherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/*
* 기상청 초단기예보조회
* */
@FeignClient(
        name = "PublicDataWeather",
        url = "${spring.cloud.openfeign.client.config.public-weather.url}")
public interface PublicDataWeatherApi {
    @GetMapping(value = "/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?" +
            "ServiceKey={SERVICE_KEY}" +
            "&numOfRows={NUMBER_OF_ROWS}" +
            "&pageNo={PAGE_NO}" +
            "&dataType={DATA_TYPE}" +
            "&base_date={BASE_DATE}" +
            "&base_date={BASE_TIME}" +
            "&nx={NX}" +
            "&ny={NY}",
            consumes = "application/json",
            produces = "application/json")
    GetPublicDataWeatherDto getPublicDataWeather(
            @RequestHeader("Content-type") String contentType,
            @PathVariable("SERVICE_KEY") String serviceKey,
            @PathVariable("NUMBER_OF_ROWS") int numOfRows,
            @PathVariable("PAGE_NO") int pageNo,
            @PathVariable("DATA_TYPE") String dataType,
            @PathVariable("BASE_DATE") String formattedLocalDate,
            @PathVariable("BASE_TIME") String formattedLocalTime,
            @PathVariable("NX") double x,
            @PathVariable("NY") double y);
}