package Ness.Backend.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
* 초단기예보
* */
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataWeather {
    @JsonProperty("response")
    private PublicDataResponse response;
}
