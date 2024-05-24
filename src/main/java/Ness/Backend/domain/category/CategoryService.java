package Ness.Backend.domain.category;

import Ness.Backend.domain.category.dto.reponse.GetCategoryDto;
import Ness.Backend.domain.category.dto.reponse.GetCategoryListDto;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.chat.dto.response.GetChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    /* 특정 유저의 카테고리 전부 가져오기 */
    public GetCategoryListDto getOneUserCategory(Long memberId) {
        List<Category> categoryList = categoryRepository.findCategoryByMember_id(memberId);

        List<GetCategoryDto> getCategoryDtos = categoryList.stream()
                .map(category -> GetCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .color(category.getColor())
                        .build())
                .toList();
        return new GetCategoryListDto(getCategoryDtos);
    }
}
