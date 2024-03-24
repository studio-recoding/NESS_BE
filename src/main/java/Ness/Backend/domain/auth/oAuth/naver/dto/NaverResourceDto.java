package Ness.Backend.domain.auth.oAuth.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class NaverResourceDto {
    @JsonProperty("response")
    private NaverResponse naverResponse;
}