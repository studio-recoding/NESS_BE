package Ness.Backend.domain.profile.dto.request;

import Ness.Backend.domain.profile.entity.PersonaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatchPersonaDto {
    @Schema(description = "업데이트할 사용자 페르소나", example = "NESS")
    @JsonProperty("persona")
    private PersonaType persona;
}
