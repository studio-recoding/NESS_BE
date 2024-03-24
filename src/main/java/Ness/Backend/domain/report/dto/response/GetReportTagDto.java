package Ness.Backend.domain.report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetReportTagDto {
    @Schema(description = "테그 고유 인식 넘버", example = "0")
    private Long id;

    @Schema(description = "테그 생성 날짜", example = "2024-01-28 12:34:56")
    private String createdDate;

    @Schema(description = "테그 이름", example = "공부 매니아")
    private String tagTitle;

    @Schema(description = "테그 설명", example = "이번 달에 공부를 많이 하셨네요. 공부에 열정이 넘치는 당신은 공부 매니아!")
    private String tagDesc;

    @Builder
    public GetReportTagDto(Long id, String createdDate, String tagTitle, String tagDesc){
        this.id = id;
        this.createdDate = createdDate;
        this.tagTitle = tagTitle;
        this.tagDesc = tagDesc;
    }
}
