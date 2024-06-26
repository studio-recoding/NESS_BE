package Ness.Backend.domain.report.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("createdDate")
    private String createdDate;

    private String memory;

    @Builder
    public GetReportMemoryDto(Long id, String createdDate, String memory){
        this.id = id;
        this.createdDate = createdDate;
        this.memory = memory;
    }
}
