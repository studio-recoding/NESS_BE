package Ness.Backend.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMemoryListResponseDto {
    private List<ReportMemoryDto> reportMemoryDtos;
}
