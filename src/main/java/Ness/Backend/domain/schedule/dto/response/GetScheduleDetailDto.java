package Ness.Backend.domain.schedule.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetScheduleDetailDto {
    @Schema(description = "스케쥴 위치", example = "이화여대 ECC")
    private String location;

    @Schema(description = "스케쥴 사람", example = "홍길동")
    private String person;

    @Builder
    public GetScheduleDetailDto(String location, String person){
        this.location = location;
        this.person = person;
    }
}
