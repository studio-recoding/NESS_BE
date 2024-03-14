package Ness.Backend.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ReportRecommendResponseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "한줄 추천 생성 날짜", example = "2024-01-28 12:34:56")
    private String createdDate;

    @Schema(description = "한줄 추천 내용", example = "방학에는 역시 개발 공부죠!")
    private String recommendText;

    @Builder
    public ReportRecommendResponseDto(Long id, String createdDate, String recommendText){
        this.id = id;
        this.createdDate = createdDate;
        this.recommendText = recommendText;
    }
}
