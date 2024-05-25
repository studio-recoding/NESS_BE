package Ness.Backend.domain.category;

import Ness.Backend.domain.category.dto.reponse.GetCategoryListDto;
import Ness.Backend.domain.category.dto.request.PostCategoryDto;
import Ness.Backend.domain.category.dto.request.PutCategoryDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "카테고리 API", description = "사용자의 카테고리 관련 API")
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    @Operation(summary = "특정 사용자의 모든 카테고리", description = "모든 카테고리가 리스트로 반환됩니다.")
    public ResponseEntity<GetCategoryListDto> getUserCategory(@AuthUser Member member){
        GetCategoryListDto getCategoryListDto = categoryService.getUserCategory(member.getId());
        return new ResponseEntity<>(getCategoryListDto, HttpStatusCode.valueOf(200));
    }

    @PostMapping("")
    @Operation(summary = "사용자가 만든 카테고리 저장", description = "사용자가 만든 카테고리를 저장합니다.")
    public ResponseEntity<?> postUserCategory(@AuthUser Member member, @RequestBody PostCategoryDto postCategoryDto){
        categoryService.postUserCategory(member.getId(), postCategoryDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PutMapping("")
    @Operation(summary = "사용자가 만든 카테고리 수정", description = "사용자가 만든 카테고리를 수정합니다.")
    public ResponseEntity<?> putUserCategory(@AuthUser Member member, @RequestBody PutCategoryDto putCategoryDto){
        categoryService.putUserCategory(member.getId(), putCategoryDto);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
