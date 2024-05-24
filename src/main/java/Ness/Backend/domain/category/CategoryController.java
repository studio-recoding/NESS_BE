package Ness.Backend.domain.category;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.dto.response.GetScheduleListDto;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "카테고리 API", description = "사용자의 카테고리 관련 API")
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    //TODO: 만들기
    /*
    @GetMapping("")
    @Operation(summary = "특정 사용자의 한달치 스케쥴 내역", description = "&month=2024-01 와 같은 형식으로 데이터가 전달됩니다.")
    public ResponseEntity<?> getUserSchedule(@AuthUser Member member){
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
     */
}
