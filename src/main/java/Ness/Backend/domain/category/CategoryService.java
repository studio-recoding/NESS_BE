package Ness.Backend.domain.category;

import Ness.Backend.domain.category.dto.reponse.GetCategoryDto;
import Ness.Backend.domain.category.dto.reponse.GetCategoryListDto;
import Ness.Backend.domain.category.dto.request.PostCategoryDto;
import Ness.Backend.domain.category.dto.request.PutCategoryDto;
import Ness.Backend.domain.category.entity.Category;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.schedule.ScheduleRepository;
import Ness.Backend.domain.schedule.ScheduleService;
import Ness.Backend.domain.schedule.entity.Schedule;
import Ness.Backend.global.error.exception.DefaultCategoryException;
import Ness.Backend.global.error.exception.DuplicateCategoryException;
import Ness.Backend.global.error.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    /* 특정 유저의 카테고리 전부 가져오기 */
    @Transactional(readOnly = true)
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
    public void postUserCategory(Long memberId, PostCategoryDto postCategoryDto){
        List<Category> categoryList = categoryRepository.findCategoriesByMember_idAndName(memberId, postCategoryDto.getName());
        if(categoryList.isEmpty()){
            //중복되지 않은 카테고리일 경우는 그대로 저장해주기
            Member member = memberRepository.findMemberById(memberId);

            Category category = Category.builder()
                    .member(member)
                    .color(postCategoryDto.getColor())
                    .name(postCategoryDto.getName())
                    .build();

            categoryRepository.save(category);
        }
        else {
            throw new DuplicateCategoryException();
        }
    }

    /* 카테고리 수정하기 */
    public void putUserCategory(Long memberId, PutCategoryDto putCategoryDto){
        Category changeCategory = categoryRepository.findCategoryById(putCategoryDto.getId());
        checkCategoryAuth(memberId, changeCategory);

        List<Category> categoryList = categoryRepository.findCategoriesByMember_idAndNameExcludeId(memberId, putCategoryDto.getName(), putCategoryDto.getId());

        if(categoryList.isEmpty()){
            //중복되지 않은 카테고리일 경우는 변경사항 저장 가능
            log.info(putCategoryDto.getId() + "번 카테고리 " + changeCategory.getName() + " 수정");
            changeCategory.changeCategory(putCategoryDto.getName(), putCategoryDto.getColor());
        }
        else {
            throw new DuplicateCategoryException();
        }
    }

    /* 카테고리 삭제하기 */
    @Transactional
    public void deleteUserCategory(Long memberId, Long categoryId){
        Category deleteCategory = categoryRepository.findCategoryById(categoryId);
        checkCategoryAuth(memberId,deleteCategory);

        if(deleteCategory.isDefaultNone()){
            //디폴트 미분류 카테고리는 삭제 불가
            throw new DefaultCategoryException();
        } else{
            //디폴트 카테고리 찾기
            Category defaultCategory = categoryRepository.findCategoryByMember_idAndIsDefaultNone(memberId, true);

            //삭제할 카테고리의 일정은 모두 디폴트로 옮기고, 변경 사항을 VectorDB에도 저장하기
            List<Schedule> changeScheduleList = scheduleRepository.findSchedulesByCategory_Id(categoryId);

            changeScheduleList.forEach(schedule -> {
                schedule.changeCategory(defaultCategory);
                
                scheduleService.putAiSchedule(
                        schedule.getInfo(),
                        schedule.getLocation(),
                        schedule.getPerson(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        schedule.getCategory().getName(),
                        schedule.getCategory().getId(),
                        schedule.getCategory().getColor(),
                        schedule.getMember().getId(),
                        schedule.getId());
            });
            
            //해당 카테고리 삭제
            log.info(categoryId + "번 카테고리 " + deleteCategory.getName() + " 삭제");
            categoryRepository.delete(deleteCategory);
        }
    }

    //자기 자신의 리소스를 접근하고 있는지 확인
    private void checkCategoryAuth(Long memberId, Category category){
        if(!Objects.equals(memberId, category.getMember().getId())){
            throw new UnauthorizedAccessException("권한이 없습니다. 해당 카테고리는 다른 유저가 권한을 가지고 있습니다.");
        }
    }
}
