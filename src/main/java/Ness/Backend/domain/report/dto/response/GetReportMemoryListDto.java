package Ness.Backend.domain.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReportMemoryListDto {
    private List<GetReportMemoryDto> ReportMemoryList;
}
