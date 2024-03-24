package Ness.Backend.domain.report.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetReportMemoryDto {
    @Schema(description = "메모리 인식 넘버", example = "0")
    private Long id;

    @Schema(description = "채팅 고유 인식 넘버", example = "0")
    private String createdDate;

    private String pictureUrl;

    @Builder
    public GetReportMemoryDto(Long id, String createdDate, String pictureUrl){
        this.id = id;
        this.createdDate = createdDate;
        this.pictureUrl = pictureUrl;
    }
}
