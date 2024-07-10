package Ness.Backend.domain.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetOnBoardingDto {
    @Schema(description = "사용자의 온보딩 여부", example = "false")
    @JsonProperty("onBoarding")
    private boolean onBoarding;
}
