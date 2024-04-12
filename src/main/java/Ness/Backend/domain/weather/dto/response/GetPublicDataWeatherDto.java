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
public class GetPublicDataWeatherDto {
    @JsonProperty("numOfRows")
    private int numOfRows;

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("resultMsg")
    private String resultMsg;

    @JsonProperty("dataType")
    private String dataType;

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
    private int fcstValue;
}
