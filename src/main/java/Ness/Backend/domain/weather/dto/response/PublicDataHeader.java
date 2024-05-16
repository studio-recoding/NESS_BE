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
public class PublicDataHeader {
    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("resultMsg")
    private String resultMsg;
}
