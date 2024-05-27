package Ness.Backend.domain.category;

import Ness.Backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryById(Long id);

    List<Category> findCategoriesByMember_id(Long memberId);

    @Query(value = "SELECT * FROM category " +
            "WHERE member_id = :memberId " +
            "AND name = :name " +
            "AND category_id != :categoryId",
            nativeQuery = true)
    List<Category> findCategoriesByMember_idAndNameExcludeId(Long memberId, String name, Long categoryId);

    List<Category> findCategoriesByMember_idAndName(Long memberId, String name);

    @Query(value = "SELECT * FROM category " +
            "WHERE member_id = :memberId " +
            "AND is_default_none = :isDefaultNone " +
            "LIMIT 1",
            nativeQuery = true)
    Category findCategoryByMember_idAndIsDefaultNone(@Param("memberId") Long memberId,
                                                     @Param("isDefaultNone") boolean isDefaultNone);
}
