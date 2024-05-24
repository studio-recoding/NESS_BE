package Ness.Backend.domain.category;

import Ness.Backend.domain.category.dto.reponse.GetCategoryListDto;
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
    private final CategoryService categoryService;
    @GetMapping("")
    @Operation(summary = "특정 사용자의 모든 카테고리", description = "모든 카테고리가 리스트로 반환됩니다.")
    public ResponseEntity<GetCategoryListDto> getUserSchedule(@AuthUser Member member){
        GetCategoryListDto getCategoryListDto = categoryService.getOneUserCategory(member.getId());
        return new ResponseEntity<>(getCategoryListDto, HttpStatusCode.valueOf(200));
    }
}
