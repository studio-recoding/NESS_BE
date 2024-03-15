package Ness.Backend.domain.report;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.report.dto.ReportMemoryListResponseDto;
import Ness.Backend.domain.report.dto.ReportTagListResponseDto;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("report/memory")
    @Operation(summary = "리포트 메모리 조회 API", description = "사용자의 ID로 리포트 메모리를 조회하는 API입니다.")
    public ReportMemoryListResponseDto memory(@AuthUser Member member) {
        return reportService.getMemory(member.getId());
    }

    @GetMapping("report/tag")
    @Operation(summary = "리포트 테그 조회 API", description = "사용자의 ID로 리포트 테그를 조회하는 API입니다.")
    public ReportTagListResponseDto tag(@AuthUser Member member) {
        return reportService.getTag(member.getId());
    }

    @GetMapping("report/recommend")
    @Operation(summary = "한줄 추천 조회 API", description = "사용자의 ID로 한줄 추천을 조회하는 API 입니다.")
    public ReportTagListResponseDto recommend(@AuthUser Member member) {
        return reportService.getTag(member.getId());
    }

}
