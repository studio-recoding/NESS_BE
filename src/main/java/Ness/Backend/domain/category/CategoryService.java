package Ness.Backend.domain.category;

import Ness.Backend.domain.category.dto.reponse.GetCategoryDto;
import Ness.Backend.domain.category.dto.reponse.GetCategoryListDto;
import Ness.Backend.domain.category.dto.request.PostCategoryDto;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    /* 특정 유저의 카테고리 전부 가져오기 */
    public GetCategoryListDto getUserCategory(Long memberId) {
        List<Category> categoryList = categoryRepository.findCategoriesByMember_id(memberId);

        List<GetCategoryDto> getCategoryDtos = categoryList.stream()
                .map(category -> GetCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .color(category.getColor())
                        .build())
                .toList();
        return new GetCategoryListDto(getCategoryDtos);
    }

    /* 카테고리 새롭게 만들기(유저에 의해서만 직접 생성) */
    @Transactional
    public Long postUserCategory(Long memberId, PostCategoryDto postCategoryDto){
        List<Category> categoryList = categoryRepository.findCategoriesByName(postCategoryDto.getName());
        if(categoryList == null){
            //중복되지 않은 카테고리일 경우는 그대로 저장해주기
            Member member = memberRepository.findMemberById(memberId);

            Category category = Category.builder()
                    .member(member)
                    .color(postCategoryDto.getColor())
                    .name(postCategoryDto.getName())
                    .build();

            categoryRepository.save(category);

            return category.getId();
        }/*
        else {

        }
        */

        return 1L;
    }
}
