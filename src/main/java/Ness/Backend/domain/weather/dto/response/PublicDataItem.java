package Ness.Backend.domain.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PublicDataItem {
    @JsonProperty("baseDate")
    private int baseDate;

    @JsonProperty("baseTime")
    private int baseTime;

    @JsonProperty("nx")
    private int nx;

    @JsonProperty("ny")
    private int ny;

    @JsonProperty("category")
    private String category;

    @JsonProperty("fcstDate")
    private int fcstDate;

    @JsonProperty("fcstTime")
    private int fcstTime;

    @JsonProperty("fcstValue")
    private String fcstValue;
}
