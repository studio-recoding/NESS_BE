package Ness.Backend.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTagListResponseDto {
    private List<ReportTagDto> reportTagDtos;
}
